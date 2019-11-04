
public class BankAccount {
	private Depositor myInfo;
	private int accNum;
	private String accType;
	private double accBal;

	public Depositor getMyInfo() {
		return myInfo;
	}

	public int getAccNum() {
		return accNum;
	}

	public void setAccNum(int accNum) {
		this.accNum = accNum;
	}

	public String getAccType() {
		return accType;
	}

	public void setAccType(String accType) {
		this.accType = accType;
	}

	public double getAccBal() {
		return accBal;
	}

	public void setAccBal(double accBal) {
		this.accBal = accBal;
	}

	public void setMyInfo(Depositor myInfo) {
		this.myInfo = myInfo;
	}

}
