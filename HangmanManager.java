import java.util.*;
import java.io.*;
/** This HangmanManager keeps tab of the play.
 * The dictionary of word are filtered interms of the length of the words.
 * The words with length of the variable 'length' are added to the Treeset wordsOfLength
 * The max paramater is used to initialize the variable remaining which tells how many attempts can be made by the player.
 * The user guessed letters are stored in a Set called userGuesses which returns true if a letter entered is not in the set and false if it's in the set.
 * The String successfullLetters stores the correct guess of letters by the player and places them in the correct index as per the word choosen.
 * 
 * The method words returns the set of words which are currently used to play.
 * guessesLeft method returns the remaining attemmpts for the user to run out of the guess.
 * guesses returns a set of characters that have been guessed by the user since the start of the game.
 * The pattern method returns the arrangement of the successfulLetters with spaces between but not starting or trailing space.
 * 
 * The record method returns the number of occurrences of the guessed letter in the word. It checks the remaining and wordsOfLength and if the latter is empty,
 * and if remaining attempts is less than or equal to 0  it throws an illegalStateException  throes illegalArgumentException if user guessed a letter more than once.
 * We iterate through the current words worked on as we update. The family pattern with more elements are selected to be the one to be guessed among them when the,
 * next letter is guessed by the user.
 * */
public class HangmanManager{
	TreeSet<String> wordsOfLength = new TreeSet<>();
	int remaining;
	Set<Character> userGuesses = new TreeSet<>();
	String successfullLetters="";
	HangmanManager(Collection<String> dictionary,int length,int max)
	{
		ArrayList<String> list = new ArrayList<>(dictionary);
		if(length < 1 || max < 0) throw new IllegalArgumentException();
		remaining = max;
		for(int j=0;j<length;j++)
		{
				successfullLetters+="-";
		}
		for(int i=0;i<list.size();i++)
		{
			if(list.get(i).length()==length)
				wordsOfLength.add(list.get(i));
		}
	}

	public Set<String> words()
	{
		return wordsOfLength;
	}

	public int guessesLeft()
	{
		return remaining;
	}

	public Set<Character> guesses()
	{
		return userGuesses;
	}

	public String pattern()
	{
		if(wordsOfLength.isEmpty()) throw new IllegalStateException();
		String pattern1 ="";
		for(int i=0;i<successfullLetters.length();i++)
		{
			if(i==0)
				pattern1=successfullLetters.charAt(i)+" ";
			else if(i==successfullLetters.length()-1)
				pattern1+=successfullLetters.charAt(i);
			else
				pattern1+=successfullLetters.charAt(i)+" ";

		}
		return pattern1;
	}

	public int record(char guess)
	{
		if(remaining<0 || wordsOfLength.isEmpty()) throw new IllegalStateException();

		if(!userGuesses.add(guess)) throw new IllegalArgumentException();

		TreeMap<String,TreeSet<String>> treemapTemp= new TreeMap<String,TreeSet<String>>();
		TreeSet<String> updateWords = new TreeSet<String>();
		int occurrence = 0; // This variable is the one that tells how many occurences of a letter is in a word
		int previousOccurrence=0;
		Iterator<String> itr = wordsOfLength.iterator(); //This is the iterator for the words that are used in a game
		String builderFinal=""; //This is the string that will be used in assigning the string built in string builder
		StringBuilder builder;//This is a string builder that helps set the pattern of the letters
		while(itr.hasNext()) // We iterate through the chosen words
		{
			occurrence=0; //Set the occurences to 0
			String element = itr.next(); //We assign the itr.next to String element for manipulation
			for(int i=0;i<element.length();i++) //Count the number of occurrences
				if(element.charAt(i)==guess)
					occurrence++;

				builder = new StringBuilder(successfullLetters); //initialize the string builder to successFullLetters in the game
				if(occurrence>0) //If occurrence is more than 0 then it means the letter is successful
				{
					previousOccurrence=occurrence; //we assign the occurence to previous occurence
				
					for(int i=0;i<element.length();i++) //Loop through the letters of the word
					{
						if(builder.charAt(i)=='-' || element.charAt(i)==builder.charAt(i)) //If the builder contains a - in the index then enter the if 
						{
							if(element.charAt(i)==guess) //If the character guessed is similar to that of the word at that index then set it to that letter
								builder.setCharAt(i,element.charAt(i));
								
							else if(element.charAt(i)!=builder.charAt(i)) // else if it is not then set it to - incase there was an occurrence set when letter was 1 now maybe is more than 1 and in different  indexes
								builder.setCharAt(i,'-');							
						}
					
					}
					builderFinal=builder.toString();//Now assign builderFinal the string builder changed to string

				}
				else
					builderFinal = builder.toString();

				if(treemapTemp.containsKey(builderFinal))//Add the string to the tree map that is the to the pattern that is there like ---- or ---letter
					treemapTemp.get(builderFinal).add(element);
				else{	//If the pattern is absent then new pattern is added to the tree map
					TreeSet<String> elem = new TreeSet<String>();
					elem.add(element);
					treemapTemp.put(builderFinal,elem);
				}
		
		}


		if(previousOccurrence==0) //We only reduce the chances if the guessed letter doesn't exist
			remaining--;

		int sizeToUse=0;
		String toPut="";
		for (Map.Entry<String, TreeSet<String>> entry : treemapTemp.entrySet()) { //We change the set of words to the pattern with the largest elements
			if(entry.getValue().size()>sizeToUse)
			{
				sizeToUse=entry.getValue().size();
				wordsOfLength=entry.getValue();
				toPut=entry.getKey();
			}
        }

        builder = new StringBuilder(successfullLetters);
        for(int i=0;i<toPut.length();i++) //Update the builder so that it can be used to update the successfull letters
        {
        	builder.setCharAt(i,toPut.charAt(i));
        }
        successfullLetters=builder.toString();
		return previousOccurrence;
	}
}