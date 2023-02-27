

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class TransIdDecode {

    private static Long TIMESTAMP = 1664553600000L;
    private static DateTimeFormatter DTF_DTMS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS");

    public static void main(String[] args) {
        System.out.println(timestamp2LocalDateTime(TIMESTAMP).format(DTF_DTMS));
        int workerId = 0;
        int datacenterId = 0;
        Long timestamp = System.currentTimeMillis();
        System.out.println(timestamp + "  " + (timestamp - TIMESTAMP));
        Long a = timestamp - 1664553600000L << 22 | datacenterId << 18 | workerId << 12;

        Long _transId = 44097245045325824L;
        Long _timestamp = _transId >> 22;
        System.out.println("_timestamp  " + _timestamp + "  " + (_timestamp + TIMESTAMP) + "  " + timestamp2LocalDateTime(_timestamp + TIMESTAMP).format(DTF_DTMS));
        Long _datacenterId = _transId >> 18;
        System.out.println(_datacenterId);

        System.out.println("_transId int " + _transId);
        System.out.println("_transId binary " + Long.toBinaryString(_transId));
        System.out.println("_transId binary " + Long.toBinaryString(_transId >> 22));
        System.out.println("_transId binary 1001110010101010001101011111001010");
        System.out.println(Long.parseLong("1001110010101010001101011111001010", 2));
        System.out.println(Long.toBinaryString(4096));
        System.out.println(Long.toBinaryString(4095));
    }

    public static LocalDateTime timestamp2LocalDateTime(Long timestamp) {
//        long timestamp = System.currentTimeMillis();
//        LocalDate localDate = Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDate();
        LocalDateTime localDateTime = Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
        return localDateTime;
    }
}

// 44097245045325824L
// 1001 1100 1010 1010 0011 0101 1111 0010 1000 0000 0000 0000 0000 0000
// ---- ---- ---- ---- ---- ---- ---- ---- -- timestamp
//                                           -- -- datacenterId
//                                                -- ---- workerId
//                                                        ---- ---- ---- sequenceId


// 4095
// 1 0000 0000 0000
//   1111 1111 1111