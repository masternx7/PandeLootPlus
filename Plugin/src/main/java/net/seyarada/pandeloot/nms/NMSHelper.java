package net.seyarada.pandeloot.nms;

public class NMSHelper {

    static Boolean isOlderThanPlayerTrackEntityEventCache;
    public static boolean isOlderThanPlayerTrackEntityEvent() {
        if(isOlderThanPlayerTrackEntityEventCache!=null) return isOlderThanPlayerTrackEntityEventCache;

        String version = NMSManager.getNMSVersion();
        // Handle version format like "v1_21_7_R1" or "v1_20_R1"
        if (version.startsWith("v")) {
            version = version.substring(1); // Remove 'v' prefix
        }
        
        // Extract major and minor version numbers
        String[] parts = version.split("_");
        if (parts.length >= 2) {
            try {
                int major = Integer.parseInt(parts[0]);
                int minor = Integer.parseInt(parts[1]);
                double numericVersion = major + (minor / 100.0); // Convert to decimal format like 1.20, 1.21
                
                isOlderThanPlayerTrackEntityEventCache = numericVersion <= 1.19;
                return isOlderThanPlayerTrackEntityEventCache;
            } catch (NumberFormatException e) {
                // Fallback: assume newer version if parsing fails
                isOlderThanPlayerTrackEntityEventCache = false;
                return false;
            }
        }
        
        // Fallback: assume newer version if format is unexpected
        isOlderThanPlayerTrackEntityEventCache = false;
        return false;
    }

}
