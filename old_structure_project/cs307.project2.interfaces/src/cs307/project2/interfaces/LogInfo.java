package cs307.project2.interfaces;

/*
 * <p>
 * Information of the given user, including his/her name, staff type and password
 * <p>
 * @classname: LogInfo
*/

public record LogInfo(String name, StaffType type, String password){
    public enum StaffType {
        SustcManager,
        CompanyManager,
        Courier,
        SeaportOfficer
    }    
}
