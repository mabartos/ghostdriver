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

public class FileUploadHandler implements HttpRequestCallback {
  @Override
  public void call(HttpServletRequest req, HttpServletResponse res) throws IOException {
    if (handleFileUpload(req, res)) return;

    res.sendError(400);
  }

  private boolean handleFileUpload(HttpServletRequest req, HttpServletResponse res) throws IOException {
    if (ServletFileUpload.isMultipartContent(req) && req.getPathInfo().endsWith("/upload")) {
      // Create a factory for disk-based file items
      DiskFileItemFactory factory = new DiskFileItemFactory(1024, new File(System.getProperty("java.io.tmpdir")));

      // Create a new file upload handler
      ServletFileUpload upload = new ServletFileUpload(factory);

      // Parse the request
      List<FileItem> items;
      try {
        items = upload.parseRequest(req);
      } catch (FileUploadException fue) {
        throw new IOException(fue);
      }

      res.setHeader("Content-Type", "text/html; charset=UTF-8");
      InputStream is = items.get(0).getInputStream();
      OutputStream os = res.getOutputStream();
      IOUtils.copy(is, os);

      os.write("<script>window.top.window.onUploadDone();</script>".getBytes());

      IOUtils.closeQuietly(is);
      IOUtils.closeQuietly(os);
      return true;
    }
    return false;
  }
}
