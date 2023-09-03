package com.code.auditor.utils;

public class HTMLTemplates {
    public static final String DB_NOT_CREATED = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Error Message</title>
                <style>
                    .errorMessage {
                        color: red;
                        font-weight: bold;
                        border: 2px solid red;
                        padding: 10px;
                        margin: 10px 0;
                        border-radius: 5px;
                        background-color: #ffe6e6;
                        max-width: 400px;
                        text-align: center;
                    }
                </style>
            </head>
            <body>

            <div class="errorMessage">
                Build has failed because the project's database could not be created
            </div>

            </body>
            </html>
            """;

    public static final String SPOTBUGS_NOT_FOUND = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Error Message</title>
                <style>
                    .errorMessage {
                        color: red;
                        font-weight: bold;
                        border: 2px solid red;
                        padding: 10px;
                        margin: 10px 0;
                        border-radius: 5px;
                        background-color: #ffe6e6;
                        max-width: 400px;
                        text-align: center;
                    }
                </style>
            </head>
            <body>

            <div class="errorMessage">
                Report creation has failed! </br>
                Please check that the submission is in the following format: </br>
                Technology: Java </br>
                Build Tool: Apache Maven </br>
            </div>

            </body>
            </html>
            """;

    public static String convertToHtml(String text) {
        StringBuilder html = new StringBuilder("<html><body><pre>");

        String escapedText = text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\n", "<br>")
                .replace("\t", "&emsp;")
                .replace(" ", "&nbsp;");

        html.append(escapedText);
        html.append("</pre></body></html>");
        return html.toString();
    }
}
