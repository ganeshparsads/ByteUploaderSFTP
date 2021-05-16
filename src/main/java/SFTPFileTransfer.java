import com.jcraft.jsch.*;

import java.io.ByteArrayInputStream;

public class SFTPFileTransfer {
    private static final String REMOTE_HOST = "sftp.host.com";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "pwd";
    private static final int REMOTE_PORT = 22;
    private static final int SESSION_TIMEOUT = 10000;
    private static final int CHANNEL_TIMEOUT = 5000;

    public static void main(String[] args) {
        // String localFile = "/Users/ganesh/Desktop/random.json";
        String remoteFile = "/files/test/documents.jsonl";

        String prodDoc ="{\"selling_price\": 41.93,\"promoMessage\": \"Price Reflects 30% Off\",\"displaySalePrice\": \"$41.93\",\"title\": \"soft knit midi slip dress\",\"imageUrl\": [\"https://images.com/?&defaultImage=Photo-Coming-Soon\"],\"displayPrice\": \"$59.90\",\"uniqueId\": \"XYZ\"}";
        byte[] bytes = prodDoc.getBytes();
        Session jschSession = null;

        try {

            JSch jsch = new JSch();
            jsch.setKnownHosts("/Users/ganesh/.ssh/known_hosts");
            jschSession = jsch.getSession(USERNAME, REMOTE_HOST, REMOTE_PORT);

            // authenticate using private key
            // jsch.addIdentity("/Users/ganesh/.ssh/id_rsa");

            // authenticate using password
            jschSession.setPassword(PASSWORD);

            // 10 seconds session timeout
            jschSession.connect(SESSION_TIMEOUT);

            Channel sftp = jschSession.openChannel("sftp");

            // 5 seconds timeout
            sftp.connect(CHANNEL_TIMEOUT);

            ChannelSftp channelSftp = (ChannelSftp) sftp;

            // transfer file from local to remote server
//            channelSftp.put(localFile, remoteFile);

            // download file from remote server to local
//            channelSftp.get(remoteFile, localFile);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            channelSftp.put(byteArrayInputStream, remoteFile);

            channelSftp.exit();

        } catch (JSchException | SftpException e) {

            e.printStackTrace();

        } finally {
            if (jschSession != null) {
                jschSession.disconnect();
            }
        }

        System.out.println("Done");
    }
}
