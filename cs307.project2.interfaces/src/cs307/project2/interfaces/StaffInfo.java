package cs307.project2.interfaces;

/*
 * <p>
 * Full information of a given staff, its LogInfo is included here.
 * <p>
 * @classname: StaffInfo
 */

public record StaffInfo(LogInfo basicInfo, String company, String city, boolean isFemale, int age, String phoneNumber) {
}