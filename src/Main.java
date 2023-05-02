import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	
	private static Process currentLaunchProcess;
	
	public static void main(String[] args)
	{
		Runtime.getRuntime().addShutdownHook(new Thread(() -> killExistingProcess()));
		
		String launchCommand = "";
		for(String s : args)
		{
			launchCommand += s + " ";
		}
		launchCommand = launchCommand.trim();
		if(args.length > 0)
		{
			try {
				currentLaunchProcess = executeCommand(launchCommand, true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		while(true)
		{
			try {
				executeAndWaitFor("git fetch");
				String localCurrentHash = executeAndWaitFor("git rev-parse --short HEAD");
				String remoteCurrentHash = executeAndWaitFor("git rev-parse --short origin/master");
				
				if(localCurrentHash.trim().equalsIgnoreCase(remoteCurrentHash.trim()) == false)
				{
					String output = executeAndWaitFor("git pull origin master");
					System.out.println("updated from remote - " + output);
					
					if(args.length > 0)
					{
						killExistingProcess();
						currentLaunchProcess = executeCommand(launchCommand, true);
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(60 * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private static void killExistingProcess() {
		System.out.println("Killing child process " + currentLaunchProcess);
		if(currentLaunchProcess != null)
		{
			try {
				currentLaunchProcess.destroy();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			currentLaunchProcess = null;
		}
	}
	
	private static Process executeCommand(String command, boolean inheritIO) throws IOException
	{
		System.out.println("Executing command: " + command);
		String[] cmd = command.split(" ");
		
		ProcessBuilder ps=new ProcessBuilder(cmd);
		ps.redirectErrorStream(true);
		if(inheritIO)
			ps.inheritIO();
		
		Process pr = ps.start();
		
		return pr;
	}
	
	
	private static String executeAndWaitFor(String command) throws IOException
	{

		Process pr = executeCommand(command, false);
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String line;
		String output = "";
		
		while((line = reader.readLine()) != null)
			output += line;
		
		reader.close();
		
		try {
			pr.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return output;
	}
	
}
