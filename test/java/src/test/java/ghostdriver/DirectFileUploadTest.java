/*
This file is part of the GhostDriver by Ivan De Marino <http://ivandemarino.me>.

Copyright (c) 2012-2014, Ivan De Marino <http://ivandemarino.me>
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice,
      this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright notice,
      this list of conditions and the following disclaimer in the documentation
      and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package ghostdriver;

import ghostdriver.server.FileUploadHandler;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import static org.apache.commons.io.FileUtils.readFileToString;
import static org.apache.commons.io.FileUtils.writeStringToFile;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.textToBePresentInElementLocated;

public class DirectFileUploadTest extends BaseTest {
    private static final Logger LOG = Logger.getLogger(DirectFileUploadTest.class.getName());

    @Rule
    public TestWithServer server = new TestWithServer();

    private static final String LOREM_IPSUM_TEXT = "lorem ipsum dolor sit amet";

    @Test
    public void checkFileUploadCompletes() throws IOException {
        WebDriver d = getDriver();
        if (!(d instanceof PhantomJSDriver)) {
            // Skip this test if not using PhantomJS.
            // The command under test is only available when using PhantomJS
            return;
        }
        PhantomJSDriver phantom = (PhantomJSDriver)d;

        String buttonId = "upload";

        File testFile = createFileForUploading();

        server.setHttpHandler("POST", new FileUploadHandler());

        // Upload the temp file
        phantom.get(server.getBaseUrl() + "/common/upload.html");

        phantom.executePhantomJS("var page = this; page.uploadFile('input#"+ buttonId +"', '"+ testFile.getAbsolutePath() +"');");

        phantom.findElement(By.id("go")).submit();

        // Uploading files across a network may take a while, even if they're really small.
        // Wait for the loading label to disappear.
        wait.until(invisibilityOfElementLocated(By.id("upload_label")));

        phantom.switchTo().frame("upload_target");

        wait.until(textToBePresentInElementLocated(By.xpath("//body"), LOREM_IPSUM_TEXT));

        // Navigate after file upload to verify callbacks are properly released.
        phantom.get("http://www.google.com/");
    }

    private File createFileForUploading() throws IOException {
        File testFile = new File("build/reports/DirectFileUploadTest.checkFileUploadCompletes." + System.currentTimeMillis() + ".html");
        writeStringToFile(testFile, "<div>" + LOREM_IPSUM_TEXT + "</div>", "UTF-8");
        LOG.info("Uploading file " + testFile.getAbsolutePath() + " with content " + readFileToString(testFile));
        return testFile;
    }
}
