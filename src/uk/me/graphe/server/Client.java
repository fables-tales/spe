package uk.me.graphe.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * a class to represent the state of a client on the server
 * 
 * holds a key to the representation of the current graph it is dealing with at
 * the current time
 * 
 * @author Sam Phippen <samphippen@googlemail.com>
 * 
 */
public class Client {

	private SocketChannel mChannel;
	private ByteBuffer mReadBuffer = ByteBuffer.allocate(1024);
	private boolean mConnected = true;
    private int mSelectedGraphId;
    private int mSelectedStateId;

	public Client(SocketChannel clientSock) {
		mChannel = clientSock;
	}

	public SocketChannel getChannel() {
		return mChannel;
	}

	/**
	 * possible side effect of disconnecting the client
	 * 
	 * @return the next messages from this client
	 */
	public List<String> readNextMessages() {
		try {
			int read = mChannel.read(mReadBuffer);

			if (read == -1) {
				ClientManager.getInstance().disconnect(this);
				return null;
			} else {
				// get messages
				StringBuilder sb = new StringBuilder();
				while (read == 1024) {
					sb.append(mReadBuffer.array());
					mReadBuffer.clear();
					read = mChannel.read(mReadBuffer);
				}

				sb.append(mReadBuffer.array());
				return processMessages(sb);
			}
			
			

		} catch (IOException e) {
			throw new Error(e);
		}

	}

	private List<String> processMessages(StringBuilder sb) {
		int start = 0;
		List<String> result = new ArrayList<String>();

		while (start < sb.length()) {
			int nullIndex = sb.indexOf("\0", start);
			String ss = sb.substring(0, nullIndex - 1);
			result.add(ss);
			start = nullIndex + 1;
		}
		
		return result;
	}

	public boolean isConnected() {
		return mConnected;
	}

	public void disconnect() {
		mConnected = false;
	}

    public int getCurrentGraphId() {
        return mSelectedGraphId;
    }


}
