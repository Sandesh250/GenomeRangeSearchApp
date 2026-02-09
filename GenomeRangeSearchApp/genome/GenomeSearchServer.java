package genome;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * GenomeSearchServer - Standalone HTTP Server for Genome Range Search
 * 
 * This uses Java's built-in HttpServer (no Tomcat required!)
 * 
 * Features:
 * - Serves static files (HTML, CSS)
 * - Handles search requests using Interval Tree
 * - Runs on localhost:8080
 * 
 * Time Complexity for search: O(log n + k)
 * 
 * @author DSA-EL Project
 */
public class GenomeSearchServer {

    private static IntervalTree intervalTree;
    private static String basePath;

    public static void main(String[] args) throws IOException {
        // Get the directory where server is running
        basePath = System.getProperty("user.dir");
        
        // Initialize Interval Tree with sample genome data
        initializeIntervalTree();
        
        // Create HTTP server on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        
        // Route handlers
        server.createContext("/", new StaticFileHandler());      // Serve HTML/CSS
        server.createContext("/search", new SearchHandler());    // Handle search
        
        server.setExecutor(null); // Use default executor
        server.start();
        
        System.out.println("========================================");
        System.out.println("  Genome Range Search Engine Started!");
        System.out.println("========================================");
        System.out.println("  Open browser: http://localhost:8080");
        System.out.println("  Press Ctrl+C to stop");
        System.out.println("========================================");
    }

    /**
     * Initialize the Interval Tree with hardcoded genome dataset.
     * 
     * Dataset:
     * - BRCA1 [100, 300] - Breast cancer gene
     * - TP53  [250, 400] - Tumor protein p53
     * - MYC   [500, 650] - MYC proto-oncogene
     * - EGFR  [700, 900] - Epidermal growth factor receptor
     * - KRAS  [850, 1000] - KRAS proto-oncogene
     */
    private static void initializeIntervalTree() {
        intervalTree = new IntervalTree();
        intervalTree.insert(100, 300, "BRCA1");
        intervalTree.insert(250, 400, "TP53");
        intervalTree.insert(500, 650, "MYC");
        intervalTree.insert(700, 900, "EGFR");
        intervalTree.insert(850, 1000, "KRAS");
        System.out.println("Interval Tree initialized with 5 genes.");
    }

    /**
     * Handler for static files (index.html, styles.css)
     */
    static class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            
            // Default to index.html
            if (path.equals("/")) {
                path = "/index.html";
            }
            
            // Determine file to serve
            String filePath = basePath + path.replace("/", File.separator);
            File file = new File(filePath);
            
            if (file.exists() && file.isFile()) {
                // Determine content type
                String contentType = "text/html";
                if (path.endsWith(".css")) {
                    contentType = "text/css";
                }
                
                // Read and send file
                byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));
                exchange.getResponseHeaders().set("Content-Type", contentType);
                exchange.sendResponseHeaders(200, fileBytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(fileBytes);
                os.close();
            } else {
                // 404 Not Found
                String response = "File not found: " + path;
                exchange.sendResponseHeaders(404, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }

    /**
     * Handler for /search endpoint
     * 
     * Accepts: queryStart and queryEnd parameters
     * Returns: HTML page with overlapping genes
     * 
     * Time Complexity: O(log n + k)
     */
    static class SearchHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Parse query parameters
            String query = exchange.getRequestURI().getQuery();
            int queryStart = -1, queryEnd = -1;
            
            if (query != null) {
                for (String param : query.split("&")) {
                    String[] pair = param.split("=");
                    if (pair.length == 2) {
                        if (pair[0].equals("queryStart")) {
                            try { queryStart = Integer.parseInt(pair[1]); } catch (NumberFormatException e) {}
                        } else if (pair[0].equals("queryEnd")) {
                            try { queryEnd = Integer.parseInt(pair[1]); } catch (NumberFormatException e) {}
                        }
                    }
                }
            }
            
            String response;
            if (queryStart < 0 || queryEnd < 0 || queryStart > queryEnd) {
                response = generateErrorPage("Invalid input. Please enter valid start and end positions.");
            } else {
                // Perform Interval Tree search - O(log n + k)
                List<IntervalNode> results = intervalTree.overlapSearch(queryStart, queryEnd);
                response = generateResultPage(queryStart, queryEnd, results);
            }
            
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, response.getBytes("UTF-8").length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes("UTF-8"));
            os.close();
        }

        private String generateResultPage(int queryStart, int queryEnd, List<IntervalNode> results) {
            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html><html lang=\"en\"><head>");
            html.append("<meta charset=\"UTF-8\">");
            html.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
            html.append("<title>Search Results - Genome Range Search</title>");
            html.append("<link rel=\"stylesheet\" href=\"styles.css\">");
            html.append("</head><body>");
            html.append("<div class=\"container\">");
            html.append("<h1>🧬 Genome Range Search Results</h1>");
            html.append("<div class=\"query-info\">");
            html.append("<p><strong>Query Interval:</strong> [").append(queryStart).append(", ").append(queryEnd).append("]</p>");
            html.append("</div>");
            html.append("<div class=\"results\">");
            
            if (results.isEmpty()) {
                html.append("<div class=\"no-results\">");
                html.append("<p>No overlapping genes found</p>");
                html.append("</div>");
            } else {
                html.append("<h2>Overlapping Genes (").append(results.size()).append(" found)</h2>");
                html.append("<table><thead><tr><th>Gene Name</th><th>Interval</th></tr></thead><tbody>");
                for (IntervalNode node : results) {
                    html.append("<tr>");
                    html.append("<td class=\"gene-name\">").append(node.geneName).append("</td>");
                    html.append("<td>[").append(node.start).append(", ").append(node.end).append("]</td>");
                    html.append("</tr>");
                }
                html.append("</tbody></table>");
            }
            
            html.append("</div>");
            html.append("<div class=\"complexity-note\">");
            html.append("<p><strong>Time Complexity:</strong> O(log n + k) where k = ").append(results.size()).append(" overlapping intervals</p>");
            html.append("</div>");
            html.append("<a href=\"index.html\" class=\"back-button\">← New Search</a>");
            html.append("</div></body></html>");
            
            return html.toString();
        }

        private String generateErrorPage(String errorMessage) {
            StringBuilder html = new StringBuilder();
            html.append("<!DOCTYPE html><html lang=\"en\"><head>");
            html.append("<meta charset=\"UTF-8\">");
            html.append("<title>Error - Genome Range Search</title>");
            html.append("<link rel=\"stylesheet\" href=\"styles.css\">");
            html.append("</head><body>");
            html.append("<div class=\"container\">");
            html.append("<h1>🧬 Genome Range Search</h1>");
            html.append("<div class=\"error\"><p>⚠️ ").append(errorMessage).append("</p></div>");
            html.append("<a href=\"index.html\" class=\"back-button\">← Try Again</a>");
            html.append("</div></body></html>");
            return html.toString();
        }
    }
}
