import java.io.IOException;


public class Main 
{

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException 
	{
		System.out.println("Hello!");
		System.out.println("Give me a number:");
		int a = System.in.read();
		if(a == '0')
			System.out.println("Good!");
		else
			System.out.println("Bad..Very Bad..");
	}

}
