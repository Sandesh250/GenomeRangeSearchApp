package genome;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

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
                server.createContext("/", new StaticFileHandler()); // Serve HTML/CSS
                server.createContext("/search", new SearchHandler()); // Handle search
                server.createContext("/addGene", new AddGeneHandler()); // Handle add gene

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
         * Initialize the Interval Tree with 37-gene dataset.
         * Includes diverse gene types: Protein Coding, miRNA, lncRNA, Regulatory,
         * Pseudogene, snoRNA
         */
        private static void initializeIntervalTree() {
                intervalTree = new IntervalTree();

                // Protein Coding genes (Cancer-related)
                intervalTree.insert(100, 300, "BRCA1", "Chr17", "Protein Coding", "DNA repair", "Breast cancer", "");
                intervalTree.insert(250, 420, "TP53", "Chr17", "Protein Coding", "Tumor suppressor", "Multiple cancers",
                                "");
                intervalTree.insert(500, 680, "MYC", "Chr8", "Protein Coding", "Cell cycle regulation", "Leukemia", "");
                intervalTree.insert(700, 920, "EGFR", "Chr7", "Protein Coding", "Cell signaling", "Lung cancer", "");
                intervalTree.insert(850, 1020, "KRAS", "Chr12", "Protein Coding", "Signal transduction",
                                "Pancreatic cancer", "");
                intervalTree.insert(1050, 1220, "PTEN", "Chr10", "Protein Coding", "Tumor suppression", "Glioblastoma",
                                "");
                intervalTree.insert(1250, 1430, "ALK", "Chr2", "Protein Coding", "Kinase signaling", "Lung cancer", "");
                intervalTree.insert(1450, 1630, "BRAF", "Chr7", "Protein Coding", "MAPK pathway", "Melanoma", "");
                intervalTree.insert(1650, 1820, "CDKN2A", "Chr9", "Protein Coding", "Cell cycle inhibition",
                                "Skin cancer", "");
                intervalTree.insert(1850, 2030, "RB1", "Chr13", "Protein Coding", "Cell cycle control",
                                "Retinoblastoma", "");
                intervalTree.insert(2050, 2290, "NF1", "Chr17", "Protein Coding", "Signal regulation",
                                "Neurofibromatosis", "");
                intervalTree.insert(2300, 2550, "APC", "Chr5", "Protein Coding", "Tumor suppression", "Colon cancer",
                                "");
                intervalTree.insert(2580, 2760, "SMAD4", "Chr18", "Protein Coding", "TGF-beta signaling",
                                "Pancreatic cancer", "");
                intervalTree.insert(2780, 2990, "PIK3CA", "Chr3", "Protein Coding", "Cell growth regulation",
                                "Breast cancer", "");
                intervalTree.insert(3020, 3190, "VHL", "Chr3", "Protein Coding", "Hypoxia response", "Kidney cancer",
                                "");

                // miRNA genes
                intervalTree.insert(3200, 3250, "MIR21", "Chr17", "miRNA", "Post-transcriptional regulation", "Cancer",
                                "");
                intervalTree.insert(3270, 3320, "MIR155", "Chr21", "miRNA", "Immune regulation", "Lymphoma", "");
                intervalTree.insert(3340, 3390, "MIR34A", "Chr1", "miRNA", "Tumor suppression", "Cancer", "");
                intervalTree.insert(3410, 3460, "MIR10B", "Chr2", "miRNA", "Cell migration", "Breast cancer", "");
                intervalTree.insert(3480, 3530, "MIR122", "Chr18", "miRNA", "Liver metabolism", "Liver disease", "");

                // lncRNA genes
                intervalTree.insert(3550, 3750, "LINC00152", "Chr2", "lncRNA", "Gene regulation", "Gastric cancer", "");
                intervalTree.insert(3780, 3980, "HOTAIR", "Chr12", "lncRNA", "Chromatin remodeling", "Breast cancer",
                                "");
                intervalTree.insert(4000, 4200, "MALAT1", "Chr11", "lncRNA", "RNA splicing", "Metastasis", "");
                intervalTree.insert(4230, 4450, "XIST", "ChrX", "lncRNA", "X-chromosome inactivation",
                                "Epigenetic disorders", "");
                intervalTree.insert(4480, 4680, "NEAT1", "Chr11", "lncRNA", "Nuclear structure", "Cancer", "");

                // Regulatory elements
                intervalTree.insert(4700, 4850, "REG1", "Chr6", "Regulatory", "Transcription regulation",
                                "Metabolic disorder", "");
                intervalTree.insert(4870, 5020, "REG2", "Chr8", "Regulatory", "Enhancer activity", "None", "");
                intervalTree.insert(5050, 5200, "REG3", "Chr4", "Regulatory", "Gene expression control",
                                "Immune disorder", "");
                intervalTree.insert(5220, 5370, "REG4", "Chr10", "Regulatory", "Promoter regulation", "Cancer", "");
                intervalTree.insert(5400, 5550, "REG5", "Chr14", "Regulatory", "Chromatin accessibility", "None", "");

                // Pseudogenes
                intervalTree.insert(5580, 5700, "PSEUD1", "Chr1", "Pseudogene", "Inactive gene copy", "None", "");
                intervalTree.insert(5720, 5850, "PSEUD2", "Chr5", "Pseudogene", "Gene relic", "None", "");
                intervalTree.insert(5870, 6000, "PSEUD3", "Chr9", "Pseudogene", "Nonfunctional duplication", "None",
                                "");

                // snoRNA genes
                intervalTree.insert(6020, 6100, "SNORD1", "Chr15", "snoRNA", "rRNA modification", "None", "");
                intervalTree.insert(6120, 6200, "SNORD2", "Chr16", "snoRNA", "RNA processing", "None", "");
                intervalTree.insert(6220, 6300, "SNORD3", "Chr17", "snoRNA", "Ribosome biogenesis", "None", "");

                System.out.println("Interval Tree initialized with 37 genes.");
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
                                                        try {
                                                                queryStart = Integer.parseInt(pair[1]);
                                                        } catch (NumberFormatException e) {
                                                        }
                                                } else if (pair[0].equals("queryEnd")) {
                                                        try {
                                                                queryEnd = Integer.parseInt(pair[1]);
                                                        } catch (NumberFormatException e) {
                                                        }
                                                }
                                        }
                                }
                        }

                        String response;
                        if (queryStart < 0 || queryEnd < 0 || queryStart > queryEnd) {
                                response = generateErrorPage(
                                                "Invalid input. Please enter valid start and end positions.");
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
                        html.append("<style>");
                        html.append(".details-content { display: none; margin-top: 1rem; padding-top: 1rem; border-top: 2px solid #f0f0f0; }");
                        html.append(".details-content.show { display: block; }");
                        html.append(".details-btn { margin-top: 1rem; padding: 0.5rem 1rem; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; border: none; border-radius: 6px; cursor: pointer; font-size: 0.85rem; font-weight: 600; transition: all 0.3s; }");
                        html.append(".details-btn:hover { transform: translateY(-2px); box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3); }");
                        html.append(".detail-row { display: flex; justify-content: space-between; margin-bottom: 0.5rem; font-size: 0.9rem; }");
                        html.append(".detail-label { font-weight: 600; color: #555; }");
                        html.append(".detail-value { color: #777; }");
                        html.append("</style>");
                        html.append("</head><body>");

                        // Header
                        html.append("<header class=\"dashboard-header\">");
                        html.append("<div class=\"header-content\">");
                        html.append("<h1>🧬 Genome Range Search Engine</h1>");
                        html.append("<p>Search Results</p>");
                        html.append("</div></header>");

                        // Main container
                        html.append("<div class=\"dashboard-container\">");

                        // Query info card
                        html.append("<div class=\"search-card\" style=\"margin-bottom: 2rem;\">");
                        html.append("<h2 style=\"color: #667eea; margin-bottom: 1rem;\">Query Interval: [")
                                        .append(queryStart).append(", ").append(queryEnd).append("]</h2>");
                        html.append("<p style=\"color: #666; font-size: 1.1rem;\">Found <strong>")
                                        .append(results.size()).append(" overlapping gene(s)</strong></p>");
                        html.append("<p style=\"color: #888; margin-top: 0.5rem; font-size: 0.9rem;\">Time Complexity: O(log n + k) where k = ")
                                        .append(results.size()).append("</p>");
                        html.append("</div>");

                        if (results.isEmpty()) {
                                html.append("<div class=\"search-card\">");
                                html.append("<p style=\"text-align: center; color: #999; font-size: 1.2rem; padding: 2rem;\">No overlapping genes found</p>");
                                html.append("</div>");
                        } else {
                                // Results grid
                                html.append("<div class=\"gene-grid\">");

                                for (int i = 0; i < results.size(); i++) {
                                        IntervalNode node = results.get(i);
                                        String geneId = "gene-" + i;

                                        // Determine gene type class
                                        String typeClass = "protein-coding";
                                        if (node.type.equals("miRNA"))
                                                typeClass = "mirna";
                                        else if (node.type.equals("lncRNA"))
                                                typeClass = "lncrna";
                                        else if (node.type.equals("Regulatory"))
                                                typeClass = "regulatory";
                                        else if (node.type.equals("Pseudogene"))
                                                typeClass = "pseudogene";
                                        else if (node.type.equals("snoRNA"))
                                                typeClass = "snorna";

                                        html.append("<div class=\"gene-card\">");

                                        // Gene header
                                        html.append("<div class=\"gene-header\">");
                                        html.append("<h3>").append(node.geneName).append("</h3>");
                                        html.append("<span class=\"gene-type ").append(typeClass).append("\">")
                                                        .append(node.type).append("</span>");
                                        html.append("</div>");

                                        // Gene info (basic)
                                        html.append("<div class=\"gene-info\">");
                                        html.append("<p class=\"interval\">[").append(node.start).append(", ")
                                                        .append(node.end).append("]</p>");
                                        html.append("<p class=\"chromosome\">").append(node.chromosome).append("</p>");
                                        html.append("<p class=\"function\">").append(node.function).append("</p>");
                                        html.append("<p class=\"disease\">").append(node.diseaseAssociation)
                                                        .append("</p>");
                                        html.append("</div>");

                                        // More details button
                                        html.append("<button class=\"details-btn\" onclick=\"toggleDetails('")
                                                        .append(geneId).append("')\">More Details</button>");

                                        // Expandable details section
                                        html.append("<div id=\"").append(geneId)
                                                        .append("\" class=\"details-content\">");
                                        html.append("<div class=\"detail-row\"><span class=\"detail-label\">Full Interval:</span><span class=\"detail-value\">[")
                                                        .append(node.start).append(", ").append(node.end)
                                                        .append("]</span></div>");
                                        html.append("<div class=\"detail-row\"><span class=\"detail-label\">Chromosome:</span><span class=\"detail-value\">")
                                                        .append(node.chromosome).append("</span></div>");
                                        html.append("<div class=\"detail-row\"><span class=\"detail-label\">Gene Type:</span><span class=\"detail-value\">")
                                                        .append(node.type).append("</span></div>");
                                        html.append("<div class=\"detail-row\"><span class=\"detail-label\">Function:</span><span class=\"detail-value\">")
                                                        .append(node.function).append("</span></div>");
                                        html.append("<div class=\"detail-row\"><span class=\"detail-label\">Disease Association:</span><span class=\"detail-value\">")
                                                        .append(node.diseaseAssociation).append("</span></div>");

                                        if (node.applications != null && !node.applications.isEmpty()) {
                                                html.append("<div class=\"detail-row\"><span class=\"detail-label\">Applications:</span><span class=\"detail-value\">")
                                                                .append(node.applications).append("</span></div>");
                                        }

                                        html.append("<div class=\"detail-row\"><span class=\"detail-label\">Overlap Length:</span><span class=\"detail-value\">")
                                                        .append(Math.min(queryEnd, node.end)
                                                                        - Math.max(queryStart, node.start) + 1)
                                                        .append(" bp</span></div>");
                                        html.append("</div>");

                                        html.append("</div>"); // Close gene-card
                                }

                                html.append("</div>"); // Close gene-grid
                        }

                        // Back button
                        html.append("<div style=\"text-align: center; margin-top: 2rem;\">");
                        html.append("<a href=\"index.html\" style=\"display: inline-block; padding: 0.75rem 2rem; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; text-decoration: none; border-radius: 8px; font-weight: 600; transition: all 0.3s;\">← New Search</a>");
                        html.append("</div>");

                        html.append("</div>"); // Close dashboard-container

                        // Footer
                        html.append("<footer class=\"dashboard-footer\">");
                        html.append("<p>Genome Range Search Engine | Interval Tree Implementation</p>");
                        html.append("</footer>");

                        // JavaScript for toggle
                        html.append("<script>");
                        html.append("function toggleDetails(id) {");
                        html.append("  var element = document.getElementById(id);");
                        html.append("  element.classList.toggle('show');");
                        html.append("  var btn = event.target;");
                        html.append("  btn.textContent = element.classList.contains('show') ? 'Hide Details' : 'More Details';");
                        html.append("}");
                        html.append("</script>");

                        html.append("</body></html>");

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

        /**
         * Handler for /addGene endpoint - Adds new gene to interval tree
         */
        static class AddGeneHandler implements HttpHandler {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                        if ("POST".equals(exchange.getRequestMethod())) {
                                // Read POST data
                                InputStream is = exchange.getRequestBody();
                                String postData = new String(is.readAllBytes(), StandardCharsets.UTF_8);

                                // Parse form data
                                Map<String, String> params = new HashMap<>();
                                String[] pairs = postData.split("&");
                                for (String pair : pairs) {
                                        String[] keyValue = pair.split("=");
                                        if (keyValue.length == 2) {
                                                params.put(
                                                                java.net.URLDecoder.decode(keyValue[0],
                                                                                StandardCharsets.UTF_8),
                                                                java.net.URLDecoder.decode(keyValue[1],
                                                                                StandardCharsets.UTF_8));
                                        }
                                }

                                try {
                                        // Extract and validate parameters
                                        String geneName = params.get("geneName");
                                        int startPos = Integer.parseInt(params.get("startPos"));
                                        int endPos = Integer.parseInt(params.get("endPos"));
                                        String chromosome = params.get("chromosome");
                                        String geneType = params.get("geneType");
                                        String function = params.get("function");
                                        String disease = params.get("disease");
                                        String applications = params.getOrDefault("applications", "");

                                        // Validate
                                        if (startPos >= endPos) {
                                                sendJsonResponse(exchange, false,
                                                                "Start position must be less than end position", null);
                                                return;
                                        }

                                        // Add to interval tree
                                        intervalTree.insert(startPos, endPos, geneName, chromosome, geneType, function,
                                                        disease, applications);

                                        // Create response JSON with gene data
                                        String geneJson = String.format(
                                                        "{\"name\":\"%s\",\"start\":%d,\"end\":%d,\"chromosome\":\"%s\",\"type\":\"%s\",\"function\":\"%s\",\"disease\":\"%s\",\"applications\":\"%s\"}",
                                                        geneName, startPos, endPos, chromosome, geneType, function,
                                                        disease, applications);

                                        sendJsonResponse(exchange, true, "Gene added successfully!", geneJson);

                                        System.out.println("Added new gene: " + geneName + " [" + startPos + ", "
                                                        + endPos + "]");

                                } catch (NumberFormatException e) {
                                        sendJsonResponse(exchange, false, "Invalid number format for positions", null);
                                } catch (Exception e) {
                                        sendJsonResponse(exchange, false, "Error adding gene: " + e.getMessage(), null);
                                }
                        } else {
                                exchange.sendResponseHeaders(405, -1); // Method not allowed
                        }
                }

                private void sendJsonResponse(HttpExchange exchange, boolean success, String message, String geneData)
                                throws IOException {
                        String json;
                        if (success && geneData != null) {
                                json = String.format("{\"success\":true,\"message\":\"%s\",\"gene\":%s}", message,
                                                geneData);
                        } else {
                                json = String.format("{\"success\":false,\"message\":\"%s\"}", message);
                        }

                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.sendResponseHeaders(200, json.getBytes().length);
                        OutputStream os = exchange.getResponseBody();
                        os.write(json.getBytes());
                        os.close();
                }
        }
}
