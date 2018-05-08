package msg;



public class OrderMsg {
    private final String _account;
    private final String _exchange;
    private final String _symbol;
    private final String _side;
    private final String _qty;
    private final String _tmprice;
    private final String _exectype;
    private final String _orderStatus;
    private final String _offset;
    private final String _leavesQty;
    private final String _hedgeFlag;
    private final String _exchange_name;
    private final String _order_id;
    private final String _bar=" ";

    public OrderMsg(String _account, String _symbol, String _side, String _qty, String _tmprice,
                    String _exectype, String _orderStatus, String _offset, String _hedgeFlag , String _order_id) {
        this._account = _account;
        this._symbol = _symbol;
        this._side = _side;
        this._qty = _qty;
        this._tmprice = _tmprice;
        this._exectype = _exectype;
        this._orderStatus = _orderStatus;
        this._offset = _offset;
        this._hedgeFlag = _hedgeFlag;
        _exchange = null;
        _leavesQty = null;
        _exchange_name  = null;
        this._order_id = _order_id;
    }

    public OrderMsg(String _account, String _exchange, String _symbol, String _side, String _qty,
                    String _tmprice, String _exectype, String _orderStatus, String _offset, String _leavesQty,
                    String _hedgeFlag, String _exchange_name, String _order_id) {
        this._account = _account;
        this._exchange = _exchange;
        this._symbol = _symbol;
        this._side = _side;
        this._qty = _qty;
        this._tmprice = _tmprice;
        this._exectype = _exectype;
        this._orderStatus = _orderStatus;
        this._offset = _offset;
        this._leavesQty = _leavesQty;
        this._hedgeFlag = _hedgeFlag;
        this._exchange_name = _exchange_name;
        this._order_id = _order_id;
    }

    public String get_symbol() {
        return _symbol;
    }

    public String get_side() {
        return _side;
    }

    public String get_qty() {
        return _qty;
    }

    public String get_tmprice() {
        return _tmprice;
    }

    public String get_exectype() {
        return _exectype;
    }

    public String get_orderStatus() {
        return _orderStatus;
    }

    public String get_offset() {
        return _offset;
    }

    public String get_leavesQty() {
        return _leavesQty;
    }

    public String get_hedgeFlag() {
        return _hedgeFlag;
    }

    public String get_exchange_name() {
        return _exchange_name;
    }

    public String get_order_id() {
        return _order_id;
    }



    public String get_exchange() {
        return _exchange;
    }

    public OrderMsg(String account) {
        _account = account;
        _exchange = null;
        _symbol = null;
        _side = null;
        _qty = null;
        _tmprice = null;
        _exectype = null;
        _orderStatus = null;
        _offset = null;
        _leavesQty = null;
        _hedgeFlag = null;
        _exchange_name = null;
        _order_id = null;

    }

    public String get_account() {
        return _account;
    }

    public String toString() {
        String str_side;
        if (_side.equals("1")) {
            str_side = "B";
        } else {
            str_side = "S";
        }
        String str_hedgeFlag;
        if (_hedgeFlag.equals("0")) {
            str_hedgeFlag = "SPEC";
        } else {
            str_hedgeFlag = "HEDGE";
        }

        String str_offset;
        if ("1".equals(_offset)) {
            str_offset = "Open ";
        } else if ("2".equals(_offset)) {
            str_offset = "Close ";
        } else if ("4".equals(_offset)) {
            str_offset = "Close Intraday";
        } else {
            str_offset = "Unknow offset";
        }

        return "[ " + "Order ID: "+ _order_id + " Account: " + _account + _bar + str_hedgeFlag + _bar +  str_offset
                +  _bar +  str_side + _bar + _qty + " lots" + _bar + _symbol + " ]";
    }
}
