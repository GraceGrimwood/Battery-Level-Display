package battLvl.util;

import com.sun.jna.win32.*;
import com.sun.jna.Native;
import com.sun.jna.Structure;
import java.time.Duration;

@SuppressWarnings("unused")
public class BattLvl {
	private Kernel32.POWER_STATUS p;
	// battery level
	private String level;
	// is battery charging
	private boolean charging;
	//
	private Duration currentDur;
	//
	private Duration fullDur;
	// state of the battery as defined by
	// https://msdn.microsoft.com/en-us/library/aa373232.aspx
	private String batteryState;

	public BattLvl(Kernel32.POWER_STATUS p) {
		this.p = p;
	}

	public void retrieveLvl() {
		level = p.getBatteryLifePercent();
	}

	public void retrieveState() {
		byte flag = p.getFlag();
		switch (flag) {
		case (1):
			batteryState = "High (more than 66% remaining)";
			break;
		case (2):
			batteryState = "Low (less than 33% remaining)";
			break;
		case (4):
			batteryState = "Critical (less than 5% remaining)";
			break;
		case (8):
			batteryState = "Charging";
			break;
		case ((byte) 128):
			batteryState = "No battery connected";
			break;
		default:
			batteryState = "Unknown";
			break;
		}
	}

	public void retrieveCharging() {
		String statusStr = p.getACLineStatusString();
		switch (statusStr) {
		case "Charging":
			charging = true;
			return;
		default:
			charging = false;
			return;
		}
	}

	public void retrieveCurrentDur() {
		currentDur = p.getBatteryCurrentDuration();
	}

	public void retrieveFullDur() {
		fullDur = p.getBatteryFullDuration();
	}

	public boolean isCharging() {
		return charging;
	}

	public String getState() {
		return batteryState;
	}

	public String getLevel() {
		return level;
	}

	public Duration getFullDur() {
		return fullDur;
	}

	public Duration getCurrentDur() {
		return currentDur;
	}

	public void refresh() {
		Kernel32.INSTANCE.GetSystemPowerStatus(p);
		retrieveLvl();
		retrieveState();
		retrieveCharging();
		retrieveCurrentDur();
		retrieveFullDur();
		return;
	}
}
