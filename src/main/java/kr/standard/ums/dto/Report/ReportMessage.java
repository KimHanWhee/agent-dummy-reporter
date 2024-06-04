package kr.standard.ums.dto.Report;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReportMessage {
    private String umsMsgId; //20자리

    private String cmpMsgId; //20자리

    private String srcMsgId; //20자리

    private String receiver; //14자리

    private String sender; //14자리

    private String channel; //10자리

    private String resultCode; //5자리

    private String resultMessage;//40자리

    private String serviceProvider;//10자리

    private String srcSndDttm;

    private String pfmRcvDttm;//14자리

    private String pfmSndDttm;//14자리

    private String rptRcvDttm;
}
