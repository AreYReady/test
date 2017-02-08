package trade.xkj.com.trade.bean_;

import java.util.ArrayList;

/**
 * Created by huangsc on 2017-02-03.
 * TODO:当前所有交易的报价
 */

public class BeanAllSymbols extends BeanBaseResponse{
    ArrayList<SymbolPrices> data;
    public class SymbolPrices{

        String symbol;
        String time;
        String bid;
        String ask;
        String low;
        String high;
        String digits;
        String spread;
        Boolean sign;

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getBid() {
            return bid;
        }

        public void setBid(String bid) {
            this.bid = bid;
        }

        public String getAsk() {
            return ask;
        }

        public void setAsk(String ask) {
            this.ask = ask;
        }

        public String getLow() {
            return low;
        }

        public void setLow(String low) {
            this.low = low;
        }

        public String getHigh() {
            return high;
        }

        public void setHigh(String high) {
            this.high = high;
        }

        public String getDigits() {
            return digits;
        }

        public void setDigits(String digits) {
            this.digits = digits;
        }

        public String getSpread() {
            return spread;
        }

        public void setSpread(String spread) {
            this.spread = spread;
        }

        public Boolean getSign() {
            return sign;
        }

        public void setSign(Boolean sign) {
            this.sign = sign;
        }
    }

    public ArrayList<SymbolPrices> getData() {
        return data;
    }

    public void setData(ArrayList<SymbolPrices> data) {
        this.data = data;
    }
}
