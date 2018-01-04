package ghostdriver.server;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Logger;

public class FileUploadHandler implements HttpRequestCallback {
  private static final Logger LOG = Logger.getLogger(FileUploadHandler.class.getName());

  @Override
  public void call(HttpServletRequest req, HttpServletResponse res) throws IOException {
    if (!handleFileUpload(req, res)) {
      res.sendError(400);
    }
  }

  private boolean handleFileUpload(HttpServletRequest req, HttpServletResponse res) throws IOException {
    if (ServletFileUpload.isMultipartContent(req) && req.getPathInfo().endsWith("/upload")) {
      List<FileItem> items = parseRequest(req);

      res.setHeader("Content-Type", "text/html; charset=UTF-8");
      try (InputStream is = items.get(0).getInputStream()) {
        try (OutputStream os = res.getOutputStream()) {
          IOUtils.copy(is, os);
          os.write("<script>window.top.window.onUploadDone();</script>".getBytes());
        }
      }
      return true;
    }
    return false;
  }

  private List<FileItem> parseRequest(HttpServletRequest req) throws IOException {
    DiskFileItemFactory factory = new DiskFileItemFactory(1024, new File(System.getProperty("java.io.tmpdir")));
    ServletFileUpload upload = new ServletFileUpload(factory);

    try {
      List<FileItem> items = upload.parseRequest(req);
      LOG.info("Got files uploaded: " + items);
      return items;
    } catch (FileUploadException fue) {
      throw new IOException(fue);
    }
  }
}
