import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	
	private static Process currentLaunchProcess;
	
	public static void main(String[] args)
	{
		String launchCommand = "";
		for(String s : args)
		{
			launchCommand += s + " ";
		}
		launchCommand = launchCommand.trim();
		
		while(true)
		{
			try {
				executeAndWaitFor("git remote update");
				String statusOutput = executeAndWaitFor("git status");
				System.out.println("Current status: " + statusOutput);
				
				if(statusOutput.toLowerCase().contains("your branch is behind"))
				{
					String output = executeAndWaitFor("git pull");
					System.out.println("updated from remote - " + output);
					
					if(args.length > 0)
					{
						killExistingProcess();
						currentLaunchProcess = executeCommand(launchCommand);
					}
				}
				
			} catch (IOException e) {
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
		if(currentLaunchProcess != null)
		{
			currentLaunchProcess.destroy();
			currentLaunchProcess = null;
		}
	}
	
	private static Process executeCommand(String command) throws IOException
	{
		String[] cmd = command.split(" ");
		
		ProcessBuilder ps=new ProcessBuilder(cmd);
		ps.redirectErrorStream(true);
		
		Process pr = ps.start();  
		
		return pr;
	}
	
	
	private static String executeAndWaitFor(String command) throws IOException
	{

		Process pr = executeCommand(command);
		
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
