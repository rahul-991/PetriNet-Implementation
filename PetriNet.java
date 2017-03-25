/*
 * Name: Rahul Menon
 * UCI NetID: rmenon1
 * UCI Student ID: 29283960
 * Exam-2
 */


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

class Transitions 
{
	
	public HashMap<String, Integer> In;
	public HashMap<String, Integer> Out;
	
	Transitions()
	{
		In = new HashMap<>();
		Out = new HashMap<>();
	}
	
	void print()
	{
		System.out.println("In hashmap: ");
		for(String i:In.keySet())
			System.out.println(i+": "+In.get(i));
		
		System.out.println("Out hashmap: ");
		for(String i:Out.keySet())
			System.out.println(i+": "+Out.get(i));
	}
}

public class PetriNet 
{
	
	static HashMap<String, Integer> Places = new HashMap<>();
	static HashMap<String, Transitions> Trans = new HashMap<>();
	
	public static void FileParser(String FileName) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(FileName));
		String line = br.readLine();
		
		while (line != null)
		{
			if (line.contains("place"))
			{
				String p[] = line.split(" ");
				Places.put(p[1], Integer.parseInt(p[2]));
			}
			
			else if (line.contains("transition"))
			{
				Transitions t = new Transitions();
				String p[] = line.split(" ");
				Trans.put(p[1],t);
			}
			
			else if (line.contains("edge"))
			{
				String p[] = line.split(" ");
				
				if (Trans.containsKey(p[1]))
				{
					Transitions t = Trans.get(p[1]);
					if (t.Out.get(p[2]) == null)
						t.Out.put(p[2],1);
					else
						t.Out.put(p[2], (t.Out.get(p[2])+1));
					Trans.put(p[1], t);
				}
				
				else if (Trans.containsKey(p[2]))
				{
					Transitions t = Trans.get(p[2]);
					if (t.In.get(p[1]) == null)
						t.In.put(p[1], 1);
					else
						t.In.put(p[1], (t.In.get(p[1])+1));
					Trans.put(p[2], t);
				}
			}
			
			line = br.readLine();
		}
		br.close();
	}
	
	public static boolean CheckInput(Transitions T)
	{

		for (String i:T.In.keySet())
		{
			if(T.In.get(i) <= Places.get(i))
				continue;
			else
				return false;
		}
		
		return true;
	}
	
	
	public static void TransitionFire(Transitions T)
	{
		int edge, token;
		
		for(String i:T.In.keySet())
		{
			edge = T.In.get(i);
			token = Places.get(i);
			token = token - edge;
			Places.put(i, token);
		}
		
		for (String i: T.Out.keySet())
		{
			edge = T.Out.get(i);
			token = Places.get(i);
			token = token + edge;
			Places.put(i, token);
		}
	}

	
	public static void main(String args[]) throws IOException
	{
		
		if(args.length == 0)
		{
			System.out.println("Input the file name and iterations");
			return;
		}
		
		String FileName = args[0];
		FileParser(FileName);
		
		int iterations = Integer.parseInt(args[1]);
		int count = 0;
		int iter = 0;
		
		for(count = 1; count <= iterations; count++)
		{
			iter = 0;
			for(String i:Trans.keySet())
			{
				if (CheckInput(Trans.get(i)))
				{	
					System.out.println("Transition fired: "+i);
					TransitionFire(Trans.get(i));
					break;
				}
				else
				{
					iter += 1;
					continue;
				}
			}
			
			if (iter == Trans.size())
			{
				System.out.println("No transition can fire after "+(count-1)+" iterations");
				return;
			}
			
			System.out.println("The values of the Places after iteration number: "+count);
			
			for(String i:Places.keySet())
				System.out.println(i+": "+Places.get(i));
			
			System.out.println();
		}
		
		System.out.println(iterations+" iterations done");
	}
}