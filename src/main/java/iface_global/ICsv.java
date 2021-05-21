package iface_global;

public interface ICsv {
    String friendlyString();            // All data except for nulls
    String csvString();                 // CSV of all data
}
