package com.six.xinyidai.bean;

/**
 * Created by apple on 2017/5/1.
 */

public class UserRecord {

    private Record Record;

    public UserRecord.Record getRecord() {
        return Record;
    }

    public void setRecord(UserRecord.Record Geren) {
        Record = Geren;
    }

    public  static  class Record{
        private String userId ;
        private String prdId;
        private String channel;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getPrdId() {
            return prdId;
        }

        public void setPrdId(String prdId) {
            this.prdId = prdId;
        }

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }


        @Override
        public String toString() {
            return "Record{" +
                    "userId='" + userId + '\'' +
                    ", prdId='" + prdId + '\'' +
                    ", channel='" + channel + '\'' +
                    '}';
        }

    }
    @Override
    public String toString() {
        return "UserRecord{" +
                "Record=" + Record +
                '}';
    }
}
