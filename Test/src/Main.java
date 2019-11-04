/*
	Jeffrey Lopez Program#2
	I did extra credit #1
*/
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main {

	public static void main(String[] args) throws IOException {

		//constant definitions
		final int MAX_NUM = 50;	//maximum number of accounts
		
		//variable declarations
		int num_accts; //number of accounts
		char choice; //menu selection choice
		boolean not_done = true; //loop control flag
		
		//declare the Customer database
		BankAccount[] Customer = new BankAccount[MAX_NUM];
		
		//file for testcases
		File testFile = new File("testCases.txt");
		
		//Create a Scanner object for the testfile or keyboard
		Scanner Key = new Scanner(testFile);
		//Scanner Key = new Scanner(System.in);

		//Create output to file or console
		PrintWriter output = new PrintWriter("myoutput.txt");
		//PrintWriter output = new PrintWriter(System.out);

		/* first part */
		/* fill the database */
		num_accts = readAccts(Customer); 
		
		//print the database
		printAccts(Customer, num_accts, output);

		do {
			//print the menu
			menu();
			
			//read the selection
			choice = Key.next(".").charAt(0);
			
			//process the selection
			switch (choice) {
			case 'q':
			case 'Q':
				not_done = false;
				printAccts(Customer, num_accts, output);
				break;
			case 'b':
			case 'B':
				balance(Customer, num_accts, output, Key);
				break;
			case 'i':
			case 'I':
				accountInfo(Customer, num_accts, output, Key);
				break;
			case 'd':
			case 'D':
				Deposit(Customer, num_accts, output, Key);
				break;
			case 'w':
			case 'W':
				Withdrawal(Customer, num_accts, output, Key);
				break;
			case 'n':
			case 'N':
				num_accts = newAcct(Customer, num_accts, output, Key);
				break;
			case 'x':
			case 'X':
				num_accts = deleteAcct(Customer, num_accts, output, Key);
				break;
			default:
				output.println("Error: " + choice + 
					" is an invalid selection -  try again");
				output.println();
				output.flush();
				break;
			}
		} while (not_done);
		//close the keyboard
		Key.close();
		//close the output file
		output.close();
		//print to console to show program completion
		System.out.println();
		System.out.println("The program is terminating");
	}

	/*
	 * Method readAccts: 
	 * Inputs: Array of references Customer
	 * 
	 * Outputs: number of accts
	 * 
	 * Process: Reads information from input file into the objects and sets the
	 * values, then increases count each time and returns the value.
	 */
	public static int readAccts(BankAccount[] Customer) throws IOException {
		
		//variable declarations
		int count = 0;
		String line;
		
		//Open file for input
		File myFile = new File("input2.txt");
		//Create scanner to read input file
		Scanner readIn = new Scanner(myFile);

		while (readIn.hasNext()) {
			
			//Create objects 
			Name myName = new Name();
			Depositor myInfo = new Depositor();
			BankAccount myAcc = new BankAccount();

			//read next line of data
			line = readIn.nextLine();
			StringTokenizer myLine = new StringTokenizer(line);

			//extract the data from the line read
			myName.setFirst(myLine.nextToken());
			myName.setLast(myLine.nextToken());
			myInfo.setSSN(myLine.nextToken());
			myAcc.setAccNum(Integer.parseInt(myLine.nextToken()));
			myAcc.setAccType(myLine.nextToken());
			myAcc.setAccBal(Double.parseDouble(myLine.nextToken()));

			//set object components
			myInfo.setMyName(myName);
			myAcc.setMyInfo(myInfo);

			//set array element
			Customer[count] = myAcc;
			count++;
		}
		//close scanner
		readIn.close();
		return count;
	}

	/*
	 * Method menu: 
	 * Input: none 
	 * Process: Prints the menu of transaction choices
	 * Output: Prints the menu of transaction choices
	 */
	public static void menu() {
		System.out.println();
		System.out.println("Select one of the following transactions:");
		System.out.println("\t****************************");
		System.out.println("\t    List of Choices         ");
		System.out.println("\t****************************");
		System.out.println("\t     W -- Withdrawal");
		System.out.println("\t     D -- Deposit");
		System.out.println("\t     N -- New Account");
		System.out.println("\t     B -- Balance Inquiry");
		System.out.println("\t     I -- Account Info");
		System.out.println("\t     X -- Delete Account");
		System.out.println("\t     Q -- Quit");
		System.out.println();
		System.out.print("\tEnter your selection: ");
	}

	/*
	 * Method printAccts: 
	 * Inputs: BankAccount[] Customer - Array of references
	 * Customer num_accts - the number of accounts 
	 * output - the printwriter
	 * 
	 * Outputs: prints the information of each customer
	 * 
	 * Process: Creates objects that are used to get the values of all 
	 * customers and prints them.
	 */
	public static void printAccts(BankAccount[] Customer, int num_accts, 
			PrintWriter output) {

		//Create objects
		Name myName = new Name();
		Depositor myInfo = new Depositor();
		BankAccount myAcc = new BankAccount();

		//print table
		output.println("\t\t\t\t\t\tDatabase of Bank Accounts");
		output.println();
		output.printf("%-27s%-9s%-16s%-8s%13s", "Name", "SSN",
				 "Account Number", " Account Type", " Balance");
		output.println();

		//prints all info
		for (int count = 0; count < num_accts; count++) {
			myInfo = Customer[count].getMyInfo();
			myName = myInfo.getMyName();
			output.printf("%-12s", myName.getFirst());
			output.printf("%-12s", myName.getLast());
			output.printf("%-9s", myInfo.getSSN());
			myAcc = Customer[count];
			output.printf("%13s", myAcc.getAccNum());
			output.printf("%19s%-3s", myAcc.getAccType(), "");
			output.printf("$%9.2f", myAcc.getAccBal());
			output.println();
		}
		// flush the output file
		output.println();
		output.flush();
	}

	/*
	 * Method findAcct: 
	 * Inputs: 
	 * BankAccount[] Customer - Array of references
	 * Customer num_accts - the number of accounts 
	 * reqAccount - Account number that is being searched for
	 * 
	 * Outputs: return the index of the account number if found, if not 
	 * returns -1.
	 * 
	 * Process: Uses a linear search to find the account number
	 */
	public static int findAcct(BankAccount[] Customer, int num_accts, 
		int reqAccount) {
		for (int index = 0; index < num_accts; index++)
			if (Customer[index].getAccNum() == reqAccount)
				return index;
		return -1;
	}

	/*
	 * Method Withdrawal: 
	 * Inputs: BankAccount[] Customer - Array of references
	 * Customer num_accts - the number of accounts 
	 * output - the printwriter 
	 * key - scanner
	 * 
	 * Outputs: prints an error if account doesnt exist or withdrawal of 
	 * invalid amount. Prints info if withdrawal amount is valid.
	 * 
	 * Process: Creates a BankAccount object to subtract the withdrawal 
	 * amount from.
	 */
	public static void Withdrawal(BankAccount[] customer, int num_accts, 
			PrintWriter output, Scanner key) {

		BankAccount myAcct = new BankAccount();
		int requestedAccount;
		int index;
		double amountToWithdraw;
		double newBal = 0.0;

		System.out.println();
		// prompt for account number
		System.out.print("Enter the account number: ");
		requestedAccount = key.nextInt(); // read-in the account number
		index = findAcct(customer, num_accts, requestedAccount);

		// check if account number is valid
		if (index == -1) // invalid account
		{
			output.println("Transaction Requested: Withdrawal");
			output.println("Error: Account number " + requestedAccount + 
					" does not exist");
		} else // valid account
		{
			// prompt for amount to withdraw
			System.out.print("Enter amount to Withdraw: ");
			// read-in the amount to withdraw
			amountToWithdraw = key.nextDouble();
			myAcct = customer[index];
			if (amountToWithdraw <= 0.00) {
				// invalid amount to withdraw
				output.println("Transaction Requested: Withdrawal");
				output.println("Account Number: " + requestedAccount);
				output.printf("Current Balance: $%.2f\n", myAcct.getAccBal());
				output.printf("Amount to Withdraw: $%.2f\n", amountToWithdraw);
				output.println("Error: Can't withdraw an amount less than 0"+ 
					" or equal to 0");
			} else if (amountToWithdraw > myAcct.getAccBal()) {
				output.println("Transaction Requested: Withdrawal");
				output.println("Account Number: " + requestedAccount);
				output.printf("Current Balance: $%.2f\n", myAcct.getAccBal());
				output.printf("Amount to Withdraw: $%.2f\n", amountToWithdraw);
				output.println("Error: Insufficient Amount");
			} else {
				myAcct = customer[index];
				output.println("Transaction Requested: Withdrawal");
				output.println("Account Number: " + requestedAccount);
				output.printf("Old Balance: $%.2f\n", myAcct.getAccBal());
				output.printf("Amount to Withdraw: $%.2f\n", amountToWithdraw);
				// makes the withdrawal
				newBal = myAcct.getAccBal() - amountToWithdraw;
				myAcct.setAccBal(newBal);
				output.printf("New Balance: $%.2f\n", newBal);
			}
		}
		output.println();
		output.flush(); // flush the output buffer
	}

	/*
	 * Method Deposit: 
	 * Inputs: BankAccount[] Customer - Array of references Customer
	 * num_accts - the number of accounts 
	 * output - the printwriter 
	 * key - scanner
	 * 
	 * Outputs: prints an error if account doesnt exist or deposit of invalid
	 * amount. Prints info if deposit amount is valid.
	 * 
	 * Process: Creates objects that are used to get the values of all 
	 * customers and adds to them then prints them.
	 */
	public static void Deposit(BankAccount[] customer, int num_accts, 
			PrintWriter output, Scanner key) {

		BankAccount myAcct = new BankAccount();
		int requestedAccount;
		int index;
		double amountToDeposit;
		double newBal;

		System.out.println();
		// prompt for account number
		System.out.print("Enter the account number: ");
		requestedAccount = key.nextInt(); // read-in the account number
		// call findAcct to search if requestedAccount exists
		index = findAcct(customer, num_accts, requestedAccount);

		if (index == -1) // invalid account
		{
				output.println("Transaction Requested: Deposit");
				output.println("Error: Account number " + requestedAccount +
						 " does not exist");
		} else // valid account
		{
			// prompt for amount to deposit
			System.out.print("Enter amount to deposit: ");
			// read-in the amount to deposit
			amountToDeposit = key.nextDouble();
			if (amountToDeposit <= 0.00) {
				// invalid amount to deposit
				output.println("Transaction Requested: Deposit");
				output.println("Account Number: " + requestedAccount);
				output.printf("Amount to Deposit: $%.2f\n", amountToDeposit);
				output.printf("Error: $%.2f is an invalid amount",
					 amountToDeposit);
				output.println();
			} else {
				myAcct = customer[index];
				output.println("Transaction Requested: Deposit");
				output.println("Account Number: " + requestedAccount);
				output.printf("Old Balance: $%.2f\n", myAcct.getAccBal());
				output.printf("Amount to Deposit: $%.2f\n", amountToDeposit);
				// make the deposit
				newBal = myAcct.getAccBal() + amountToDeposit;
				myAcct.setAccBal(newBal);
				output.printf("New Balance: $%.2f", newBal);
				output.println();
			}
		}
		output.println();

		output.flush(); // flush the output buffer
	}

	/*
	 * Method newAcct: 
	 * Inputs: BankAccount[] Customer - Array of references Customer
	 * num_accts - the number of accounts 
	 * output - the printwriter 
	 * key - scanner
	 * 
	 * Outputs: returns number of accounts
	 * 
	 * Process: Creates objects that are used to set the new values of the new
	 * account and prints the new info.
	 */
	public static int newAcct(BankAccount[] customer, int num_accts, 
			PrintWriter output, Scanner key) {

		Depositor myDep = new Depositor();
		Name myName = new Name();
		BankAccount myAcct = new BankAccount();
		int newAccountNumber;
		int index;
		int tempNumAccts = num_accts;
		double newBal;
		String first;
		String last;
		String ssn;
		String accType;
		Double openAmount;

		System.out.print("Enter New Account Number: ");
		newAccountNumber = key.nextInt();

		// finds the index using the findAcct method
		index = findAcct(customer, num_accts, newAccountNumber);

		if (index == -1) {
			if (newAccountNumber <= 999999 && newAccountNumber >= 100000) {

				// If the account number is valid adds it to the array

				// makes the balance of the new account $0

				System.out.print("Enter your first name, last name, SSN," + 
							" Account type and opening deposit amount: ");
				first = key.next();
				last = key.next();
				ssn = key.next();
				accType = key.next();
				openAmount = key.nextDouble();

				if (first.isEmpty() || last.isEmpty()) {
					output.println("First and Last name is needed to "
						+"open an account");
				} else if (ssn.isEmpty()) {
					output.println("A Social Secruity Number is needed to "
						+"open an account");
				} else if (accType.isEmpty()) {
					output.println("An account type is needed to open"
						+" an account");
				} else {
					myAcct.setAccNum(newAccountNumber);
					myName.setFirst(first);
					myName.setLast(last);
					myDep.setSSN(ssn);
					myAcct.setAccType(accType);
					newBal = myAcct.getAccBal() + openAmount;
					customer[tempNumAccts++] = myAcct;
					myDep.setMyName(myName);
					myAcct.setMyInfo(myDep);
					myAcct.setAccBal(newBal);
					num_accts++; // Adds one to the counter
					output.println("Transaction Requested: New Account");
					output.println("Account number " + newAccountNumber + 
						" has been created");
					output.println("Customer name: " + first + " " + last);
					output.println("SSN: " + ssn);
					output.println("Acctount type: " + accType);
					output.println("Balance: $0.00");
					output.printf("Opening Deposit: $%.2f\n", openAmount);
					output.printf("New Balance: $%.2f\n", myAcct.getAccBal());
				}

			} else {
				output.println("Transaction Requested: New Account");
				output.println("Error: The Account number " + newAccountNumber
					 + " is too small or too large");
			}
		} else {
			output.println("Transaction Requested: New Account");
			output.println("Error: Account number " + newAccountNumber + 
				" already exists");
		}
		output.println();
		output.flush();
		return num_accts;
	}

	/*
	 * Method deleteAcct: 
	 * Inputs: BankAccount[] Customer - Array of references
	 * Customer num_accts - the number of accounts 
	 * output - the printwriter 
	 * key - scanner
	 * 
	 * Outputs: returns the number of accounts. Prints error if account doesnt
	 * exist or if account has below zero amount and trying to delete. 
	 * Prints info if account meets requirements to delete.
	 * 
	 * Process: Prompts user for Account number. Uses findAcct to find the 
	 * index. Will delete only if number is found and account is not negative.
	 * Otherwise will print error.
	 */
	public static int deleteAcct(BankAccount[] customer, int num_accts, 
		PrintWriter output, Scanner key) {

		BankAccount myAcct = new BankAccount();
		int index;
		int acctNumber;
		int tempNumAccts = num_accts;

		System.out.print("Enter Account Number: ");
		acctNumber = key.nextInt();

		// finds the index using the findAcct method
		index = findAcct(customer, num_accts, acctNumber);

		if (index == -1) { // if the AccNum is not found prints error
			output.println("Transaction Requested: Delete Account");
			output.println("Error: Account number " + acctNumber + 
					" does not exist");
		} else {
			myAcct = customer[index];
			// if balance is negative and trying to delete account
			if (myAcct.getAccBal() < 0) {
				output.println("Transaction Requested: Delete Account");
				output.println("Error: Account number " + acctNumber + 
						" has a negative balance and cannot be deleted");
			} else if(myAcct.getAccBal()>0) {
				output.println("Transaction Requested: Delete Account");
				output.printf("Error: Account number " + acctNumber + 
						" has a balance of $%.2f and cannot be deleted.\n",
						myAcct.getAccBal());
				output.println("Withdraw first then try again.");
			}else { // if account exists and doesn't have a negative balance
				output.println("Transaction Requested: Delete Account");
				output.println("Account number: " + acctNumber + 
					" has been deleted");
				for (int i = 0; i < customer.length; i++) {
					if (customer[i].getAccNum() == acctNumber) {
						for (int j = i; j < customer.length - 1; j++) {
							customer[index] = customer[tempNumAccts-1];
						}
						break;
					}
				}
				num_accts--;
			}
		}
		output.println();
		output.flush();
		return num_accts;
	}

	/*
	 * Method balance: 
	 * Inputs: 
	 * BankAccount[] Customer - Array of references Customer
	 * num_accts - the number of accounts 
	 * output - the printwriter 
	 * key - scanner
	 * 
	 * Outputs: Prints error if account is not found and if account does exist
	 * prints account balance.
	 * 
	 * Process: Prompts user for Account number. Uses findAcct to find the 
	 * index. Will show balance only if number is found. otherwise will 
	 * print error.
	 */
	public static void balance(BankAccount[] customer, int num_accts,
		 PrintWriter output, Scanner key) {

		BankAccount myAcct = new BankAccount();
		int requestedAccount;
		int index;
		// prompt for account number
		System.out.print("Enter the account number: ");
		requestedAccount = key.nextInt(); // read-in the account number

		// call findAcct to search if requestedAccount exists
		index = findAcct(customer, num_accts, requestedAccount);

		if (index == -1) // invalid account
		{
			output.println("Transaction Requested: Balance Inquiry");
			output.println("Error: Account number " + requestedAccount +
					 " does not exist");
		} else // valid account
		{
			myAcct = customer[index];
			output.println("Transaction Requested: Balance Inquiry");
			output.println("Account Number: " + requestedAccount);
			output.printf("Current Balance: $%.2f", myAcct.getAccBal());
			output.println();
		}
		output.println();
		output.flush(); // flush the output buffer
	}

	/*
	 * Method accountInfo: 
	 * Inputs: BankAccount[] Customer - Array of references
	 * Customer num_accts - the number of accounts 
	 * output - the printwriter 
	 * key - scanner
	 * 
	 * Outputs: Searches for ssn and prints out the information
	 * 
	 * Process: Prompts user for SSN. Uses a search to find it and prints 
	 * account info
	 */
	public static void accountInfo(BankAccount[] Customer, int num_accts,
			 PrintWriter output, Scanner key) {

		Name myName = new Name();
		BankAccount myAcc = new BankAccount();
		Depositor myInfo = new Depositor();

		String reqSSN;
		System.out.print("Enter SSN: ");
		reqSSN = key.next();

		for (int count = 0; count < num_accts; count++) {
			myInfo = Customer[count].getMyInfo();
			if (myInfo.getSSN().equals(reqSSN)) {
				myName = myInfo.getMyName();
				output.println("Transaction Requested: Account Info");
				output.println("Name: " + myName.getFirst() + " " + 
					myName.getLast());
				output.println("SSN: " + myInfo.getSSN());
				myAcc = Customer[count];
				output.println("Account number: " + myAcc.getAccNum());
				output.println("Account Type: " + myAcc.getAccType());
				output.println("Balance: $" + myAcc.getAccBal());
				output.println();
			}
		}
		output.flush();
	}
}
