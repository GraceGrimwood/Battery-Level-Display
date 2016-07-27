package battLvl.util;

import com.sun.jna.win32.StdCallLibrary;
import java.util.ArrayList;
import java.util.List;
import java.time.Duration;
import com.sun.jna.Native;
import com.sun.jna.Structure;

/**
 * @author Grace Grimwood
 * 
 *         Adapted from code by BalusC at StackOverflow:
 *         http://stackoverflow.com/questions/3434719/how-to-get-the-remaining-
 *         battery-life-in-a-windows-system
 */
public interface Kernel32 extends StdCallLibrary {
	public Kernel32 INSTANCE = (Kernel32) Native.loadLibrary("Kernel32", Kernel32.class);

	/**
	 * @author raven_000
	 * @see http://msdn2.microsoft.com/en-us/library/aa373232.aspx
	 */
	public class POWER_STATUS extends Structure {

		public byte ACLineStatus;
		public byte BatteryFlag;
		public byte BatteryLifePercent;
		public byte Reserved1;
		public int BatteryLifeTime;
		public int BatteryFullLifeTime;

		@Override
		protected List<String> getFieldOrder() {
			List<String> orderList = new ArrayList<String>();
			orderList.add("ACLineStatus");
			orderList.add("BatteryFlag");
			orderList.add("BatteryLifePercent");
			orderList.add("Reserved1");
			orderList.add("BatteryLifeTime");
			orderList.add("BatteryFullLifeTime");
			return orderList;
		}

		/**
		 * The AC power status
		 */
		public String getACLineStatusString() {
			switch (ACLineStatus) {
			case (0):
				return "Not charging";
			case (1):
				return "Charging";
			default:
				return "Unknown";
			}
		}

		/**
		 * The battery charge status (Method retained only for toString().)
		 */
		public String getBatteryFlagString() {
			switch (BatteryFlag) {
			case (1):
				return "High, more than 66 percent";
			case (2):
				return "Low, less than 33 percent";
			case (4):
				return "Critical, less than five percent";
			case (8):
				return "Charging";
			case ((byte) 128):
				return "No system battery";
			default:
				return "Unknown";
			}
		}

		/**
		 * The battery charge status as bytes
		 */
		public byte getFlag() {
			return BatteryFlag;
		}

		/**
		 * The percentage of full battery charge remaining
		 */
		public String getBatteryLifePercent() {
			return (BatteryLifePercent == (byte) 255) ? "Unknown" : BatteryLifePercent + "%";
		}

		/**
		 * The number of seconds of battery life remaining
		 */
		public String getBatteryLifeTime() {
			return (BatteryLifeTime == -1) ? "Unknown" : BatteryLifeTime + " seconds";
		}

		public Duration getBatteryCurrentDuration() {
			return (BatteryLifeTime == -1) ? Duration.ZERO : Duration.ofSeconds(BatteryLifeTime);
		}

		/**
		 * The number of seconds of battery life when at full charge
		 */
		public String getBatteryFullLifeTime() {
			return (BatteryFullLifeTime == -1) ? "Unknown" : BatteryFullLifeTime + " seconds";
		}

		/**
		 * The duration of battery life when at full charge
		 * 
		 * @return
		 */
		public Duration getBatteryFullDuration() {
			return (BatteryFullLifeTime == -1) ? Duration.ZERO : Duration.ofSeconds(BatteryFullLifeTime);
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("ACLineStatus: " + getACLineStatusString() + "\n");
			sb.append("Battery Flag: " + getBatteryFlagString() + "\n");
			sb.append("Battery Life: " + getBatteryLifePercent() + "\n");
			sb.append("Battery Left: " + getBatteryLifeTime() + "\n");
			sb.append("Battery Full: " + getBatteryFullLifeTime() + "\n");
			return sb.toString();
		}
	}

	public int GetSystemPowerStatus(POWER_STATUS result);
}
