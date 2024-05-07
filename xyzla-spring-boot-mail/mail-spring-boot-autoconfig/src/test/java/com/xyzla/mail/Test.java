package com.xyzla.mail;

import org.xbill.DNS.*;
import org.xbill.DNS.Record;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Test {
    public static void main(String[] args) throws TextParseException {
        String host = "showmac.com";
        // 查找mx记录
        Record[] mxRecords = new Lookup(host, Type.MX).run();
        if (mxRecords != null && mxRecords.length == 0) {
            return;
        }
        // 邮件服务器地址
        String mxHost = ((MXRecord) mxRecords[0]).getTarget().toString();
        if (mxRecords.length > 1) { // 优先级排序
            List<Record> arrRecords = new ArrayList<Record>();
            Collections.addAll(arrRecords, mxRecords);
            Collections.sort(arrRecords, new Comparator<Record>() {
                public int compare(Record o1, Record o2) {
                    if (((MXRecord) o1).getPriority() > ((MXRecord) o2).getPriority()) {
                        return 1;
                    } else if (((MXRecord) o1).getPriority() < ((MXRecord) o2).getPriority()) {
                        return -1;
                    } else {
                        return 0;
                    }
                    //return new CompareToBuilder().append(((MXRecord) o1).getPriority(), ((MXRecord) o2).getPriority()).toComparison();
                }
            });
            mxHost = ((MXRecord) arrRecords.get(0)).getTarget().toString();
            System.out.println(mxHost);
        }


    }
}
