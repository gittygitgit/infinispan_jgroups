package com.notatracer;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.util.Util;

public class SimpleChat extends ReceiverAdapter {

	JChannel channel;
	String user_name = System.getProperty("user.name", "n/a");
	final List<String> state = new LinkedList<String>();

	private void start() throws Exception {

		channel = new JChannel(); // use the default config, udp.xml

		channel.setReceiver(this);

		channel.connect("ChatCluster");

		channel.getState(null, 10000);

		eventLoop();

		channel.close();

	}

	public void viewAccepted(View new_view) {

		System.out.println("** view: " + new_view);

	}

	public void receive(Message msg) {

		String line = msg.getSrc() + ": " + msg.getObject();

		System.out.println(line);

		synchronized (state) {

			state.add(line);

		}
	}

	@Override
	public void getState(OutputStream output) throws Exception {

		synchronized (state) {

			Util.objectToStream(state, new DataOutputStream(output));

		}

	}

	public void setState(InputStream input) throws Exception {

		List<String> list;

		list = (List<String>) Util.objectFromStream(new DataInputStream(input));

		synchronized (state) {

			state.clear();

			state.addAll(list);

		}

		System.out.println(list.size() + " messages in chat history):");

		for (String str : list) {

			System.out.println(str);

		}

	}

	private void eventLoop() {

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		while (true) {

			try {

				System.out.print("> ");
				System.out.flush();

				String line = in.readLine().toLowerCase();

				if (line.startsWith("quit") || line.startsWith("exit"))

					break;

				line = "[" + user_name + "] " + line;

				Message msg = new Message(null, null, line);

				channel.send(msg);

			}

			catch (Exception e) {

			}

		}

	}

	public static void main(String[] args) throws Exception {

		new SimpleChat().start();

	}

}
