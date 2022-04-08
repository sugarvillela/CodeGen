package iface_global;

public interface ICsv {
    /** Any class that needs alt toString methods should implement this */
    String friendlyString();            // All data except for nulls
    String csvString();                 // CSV of all data
}
