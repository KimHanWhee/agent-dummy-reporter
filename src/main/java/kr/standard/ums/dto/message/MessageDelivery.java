package kr.standard.ums.dto.message;

import kr.standard.ums.dto.Fallback;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDelivery {
    private String srcMsgId;
    private String umsMsgId;
    private String callback;
    private String receiver;
    private String channel;
    private boolean fallbackFg;
    private Fallback fallback;
}
