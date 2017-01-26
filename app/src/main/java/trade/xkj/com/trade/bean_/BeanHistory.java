package trade.xkj.com.trade.bean_;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangsc on 2017-01-24.
 * TODO:历史bean
 */

public class BeanHistory {
    int status;
    String msg;
    BeanHistoryData data;

    public class BeanHistoryData {
        String symbol;
        int period;
        int barnum;
        int digits;
        ArrayList<HistoryItem> list;

        public class HistoryItem {
            int quoteTime;
            double open;
            double low;
            double high;
            double close;
            String time;

            public double getHigh() {
                return high;
            }

            public void setHigh(double high) {
                this.high = high;
            }

            public int getQuoteTime() {
                return quoteTime;
            }

            public void setQuoteTime(int quoteTime) {
                this.quoteTime = quoteTime;
            }

            public double getOpen() {
                return open;
            }

            public void setOpen(double open) {
                this.open = open;
            }

            public double getLow() {
                return low;
            }

            public void setLow(double low) {
                this.low = low;
            }

            public double getClose() {
                return close;
            }

            public void setClose(double close) {
                this.close = close;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public int getPeriod() {
            return period;
        }

        public void setPeriod(int period) {
            this.period = period;
        }

        public int getBarnum() {
            return barnum;
        }

        public void setBarnum(int barnum) {
            this.barnum = barnum;
        }

        public int getDigits() {
            return digits;
        }

        public void setDigits(int digits) {
            this.digits = digits;
        }

        public List<HistoryItem> getList() {
            return list;
        }

        public void setList(ArrayList<HistoryItem> list) {
            this.list = list;
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public BeanHistoryData getData() {
        return data;
    }

    public void setData(BeanHistoryData data) {
        this.data = data;
    }
}
