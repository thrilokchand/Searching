package tabs;

import java.awt.EventQueue;



import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.TextArea;
import java.awt.Label;
import java.awt.TextField;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import javax.swing.JTabbedPane;






public class Log_Search {
	
	

	private JFrame frame;
	/**
	 * @wbp.nonvisual location=42,499
	 */
	private final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Log_Search window = new Log_Search();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Log_Search() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 *
	 */
	
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 687, 465);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		TextArea textArea = new TextArea();
		textArea.setBounds(59, 150, 537, 109);
		frame.getContentPane().add(textArea);
		
		Label label_2 = new Label("( Not Required for searching in Staging)");
		label_2.setBounds(301, 27, 205, 22);
		frame.getContentPane().add(label_2);
		
		Label label = new Label("DATE (YYYY-MM-DD)");
		label.setBounds(10, 27, 116, 22);
		frame.getContentPane().add(label);
		
		TextField textField = new TextField();
		textField.setBounds(143, 27, 152, 22);
		frame.getContentPane().add(textField);
		
		Label label_1 = new Label("STRING PATTERN");
		label_1.setBounds(10, 65, 116, 22);
		frame.getContentPane().add(label_1);
		
		TextField textField_1 = new TextField();
		textField_1.setBounds(143, 65, 438, 22);
		frame.getContentPane().add(textField_1);
		
		TextArea textArea_1 = new TextArea();
		textArea_1.setBounds(59, 273, 537, 99);
		frame.getContentPane().add(textArea_1);
		
		
		String[] arr= new String[20];
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

		String date = simpleDateFormat.format(new Date());
		Button button_1 = new Button("Clear");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textArea.setText("");
				textField.setText("");
				textField_1.setText("");
				textArea_1.setText("");
				//arr= new String[arr.length];
			}
		});
		button_1.setBounds(436, 110, 70, 22);
		frame.getContentPane().add(button_1);
		
		Button button = new Button("Search in STG");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String str = textField.getText();
				String str2 = textField_1.getText();
				//System.out.println(str2);
				//textArea.append(str2);
				
				
				int temp = 0;
				try {
					//arr=new String[20];
					JSch ssh=new JSch();
					Session session= ssh.getSession("thrilok_chand.bogiri","10.36.77.80",22);
					session.setPassword("Fairy9036*");
					session.setConfig("StrictHostKeyChecking", "no");
					int count = 0;
					session.connect();
					textArea_1.append("Connected to SSH"+'\n');
					ChannelExec channelExec = (ChannelExec)session.openChannel("exec");
					textArea_1.append("Executing the command..." +'\n');
			        InputStream in = channelExec.getInputStream();
				    Scanner scan = new Scanner(in);
				    channelExec.setCommand("grep -l '"+str2+"' /srvrs/wac/stg/jboss-eap-6.4/domain/servers/wac*/log/wac*-wac.log*");
				    channelExec.connect();
				    textArea_1.append("Executed successfully" + '\n');
				    while(scan.hasNextLine()) {
				       	arr[count] = scan.nextLine();
				       	textArea.append(arr[count] +'\n');
				       	count++;
				     }
				  // System.out.println(arr[2]);
				 //   textArea_1.append(count.toString());
				    if(count == 0)
				    	textArea.append("We are not able to find the string in the logs");
				   
				    channelExec.disconnect();                                                                                                     
				    session.disconnect();
				    scan.close();
				
				
				}catch(JSchException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			
				
				    
			
				
				
				
						
				}
		});
		button.setBounds(126, 110, 90, 22);
		frame.getContentPane().add(button);
		
		
		
		
		Button button_2 = new Button("Download Logs");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String host="10.36.77.80"; 
				String user="thrilok_chand.bogiri";
				String passwrd="Fairy9036*";
				
				Session session;
				try {
					JSch ssh=new JSch();
					session = ssh.getSession(user,host,22);
					session.setPassword(passwrd);
					session.setConfig("StrictHostKeyChecking", "no");
					session.connect();
					ChannelExec channelExec = (ChannelExec)session.openChannel("exec");
					
					Channel channel = session.openChannel("sftp");
					channel.connect();
					ChannelSftp sftpchannel = (ChannelSftp) channel;
					if ("We are not able to find the string in the logs" == arr[0])
						textArea_1.append("There are no logs to download");
					else {
						int i=0;
						textArea_1.append("Downloading the logs....");
						while(arr[i]!=null) {
							channelExec.setCommand("zip -v /srvrs/wac/stg/logs.zip '"+arr[i]+"'");
							channelExec.connect();
							i++;
						}
						sftpchannel.get("/srvrs/wac/stg/logs.zip","C:/Users/thrilokchand.bogiri/Desktop");
						textArea_1.append("Download completed Successfully!");
				}
				sftpchannel.exit();
			    session.disconnect();
					 
				} catch (JSchException | SftpException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		button_2.setBounds(277, 394, 90, 22);
		frame.getContentPane().add(button_2);
		
		Button button_3 = new Button("Search in PRD");
		button_3.setBounds(277, 110, 90, 22);
		frame.getContentPane().add(button_3);
		
		
	}	
	
}
