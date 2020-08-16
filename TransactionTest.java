import java.io.*;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.HashMap;
import java.util.Map;

public class TransactionTest {
    public static void main(String args[]) throws IOException, ParseException {
        String fileName = "transactions.txt";
        HashMap<String, Transaction> txMap = getTxMap(fileName);
        for (Map.Entry<String, Transaction> entry : txMap.entrySet()) {
            System.out.println(entry.getValue().getAvgTime());
        }
    }

    public static HashMap<String, Transaction> getTxMap(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
        HashMap<String, Transaction> txMap = new HashMap();
        String line;
        while ((line = br.readLine()) != null) {
            String txId = line.split(",")[0].trim();
            String dateStr = line.split(",")[1].trim();
            String time = line.split(",")[2].trim();
            String txStatus = line.split(",")[3].trim();
            LocalDateTime date = getFormattedDate(dateStr + " " + time);
            Transaction transaction = new Transaction();
            if (txMap.containsKey(txId)) {
                transaction = txMap.get(txId);
            } else {
                transaction.setTxId(txId);
            }

            if (txStatus.equalsIgnoreCase("start")) {
                transaction.setStartDate(date);
            } else if ((txStatus.equalsIgnoreCase("end"))) {
                transaction.setEndDate(date);
            }
            txMap.put(txId, transaction);
        }
        return txMap;
    }

    public static LocalDateTime getFormattedDate(String dateStr) {
        dateStr = dateStr.replace("am", "AM");
        dateStr = dateStr.replace("pm", "PM");
        DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder();
        builder.parseCaseInsensitive();
        builder.appendPattern("yyyy – MM – dd h:mm a");
        DateTimeFormatter formatter = builder.toFormatter();
        LocalDateTime parsedDate = LocalDateTime.parse(dateStr, formatter);
        return parsedDate;
    }
}

class Transaction {
    private String txId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private long avgTime;

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
        if (this.startDate != null && this.endDate != null) {
            Duration duration = Duration.between(startDate, endDate);
            this.avgTime = this.avgTime + duration.toMinutes();
        }
    }

    public long getAvgTime() {
        return avgTime;
    }
}
