package featurea.desktop;

import featurea.util.StringUtil;

import java.io.*;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class SingleInstanceServer {

  public Program featureaWindow;
  private ServerSocket serverSocket;

  public SingleInstanceServer(int SERVER_PORT, File... files) throws IOException {
    try {
      serverSocket = new ServerSocket(SERVER_PORT, 10, InetAddress.getLocalHost());
      featureaWindow = newProgram();
      featureaWindow.launch();
      for (File file : files) {
        featureaWindow.openFile(file);
      }
      Thread serverThread = new Thread(new Runnable() {
        @Override
        public void run() {
          while (!serverSocket.isClosed()) {
            try {
              Socket clientSocket = serverSocket.accept();
              BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
              String filesString = reader.readLine();
              String[] tokens = StringUtil.split(filesString, ",");
              for (String token : tokens) {
                featureaWindow.openFile(new File(token));
              }
              reader.close();
              clientSocket.close();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }
      });
      serverThread.start();
    } catch (BindException e) {
      System.err.println(e.getMessage());
      Socket clientSocket = new Socket(InetAddress.getLocalHost(), SERVER_PORT);
      OutputStream out = clientSocket.getOutputStream();
      for (File file : files) {
        out.write((file.getAbsolutePath() + ",").getBytes());
      }
      out.flush();
      out.close();
      clientSocket.close();
      throw e;
    }
  }

  public abstract Program newProgram();


  public interface Program {
    void launch();

    void openFile(File file);
  }

}





