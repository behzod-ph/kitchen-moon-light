package uz.isdaha.payload.request;

import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

@Data
public class ReportRequest {
    private Timestamp fromDate;
    private Timestamp toDate;
}
