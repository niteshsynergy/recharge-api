package com.niteshsynergy.payment.upi;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum UpiProvider {
    UPI("@upi"),
    HDFC_BANK("@hdfcbank"),
    SBI("@sbi"),
    AXIS_BANK("@axisbank"),
    YES_BANK("@ybl"),
    PHONEPE_YES("@ybl"),
    PHONEPE_AXIS("@axl"),
    PAYTM("@paytm"),
    CENTRAL_BANK("@centralbank"),
    ICICI_BANK("@icici"),
    PNB("@pnb"),
    KOTAK_BANK("@kotak"),
    BOB("@barodampay"),
    UNION_BANK("@unionbank"),
    IDFC_FIRST("@idfc"),
    INDUSIND("@indus"),
    FEDERAL("@federal"),
    RBL("@rbl"),
    UCO("@uco"),
    SIB("@sib"),
    ANDHRA("@andb"),
    ALLAHABAD("@allbank"),
    OBC("@obc"),
    CORPORATION("@corp"),
    INDIAN_BANK("@indianbank"),
    CANARA("@canara"),
    KARNATAKA("@kbl"),
    DCB("@dcb"),
    IDBI("@idbi"),
    HSBC("@hsbc"),
    CITI("@citi"),
    STANDARD_CHARTERED("@sc"),
    AIRTEL("@airtel"),
    JIO("@jio"),
    AMAZON_PAY("@apl"),
    GOOGLE_PAY_HDFC("@okhdfcbank"),
    GOOGLE_PAY_SBI("@oksbi"),
    GOOGLE_PAY_ICICI("@okicici"),
    GOOGLE_PAY_AXIS("@okaxis");

    private final String suffix;

    UpiProvider(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }

    public static Set<String> getAllUpiSuffixes() {
        return Arrays.stream(values())
                .map(UpiProvider::getSuffix)
                .collect(Collectors.toSet());
    }

    public static boolean isValidUpi(String upiId) {
        return upiId != null && getAllUpiSuffixes()
                .stream()
                .anyMatch(upiId::endsWith);
    }
    // ✅ Check if the given UPI ID is valid
    public static boolean isValidUpiId(String upiId) {
        if (upiId == null || !upiId.contains("@")) {
            return false;
        }

        String[] parts = upiId.split("@");
        if (parts.length != 2) {
            return false;
        }

        String suffix = "@" + parts[1]; // Extracts the UPI suffix part
        return getAllUpiSuffixes().contains(suffix);
    }
}

