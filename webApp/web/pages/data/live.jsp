<%@ page import="engine.Engine" %>
<%@ page import="util.ServletUtils" %>
<%@ page import="data.stock.Stock" %>
<%@ page import="exception.InvalidStockSymbol" %>
<%@ page import="data.tradeOrder.order.Order" %>
<%@ page import="java.util.List" %>
<%@ page import="data.tradeOrder.list.CompletedTradesList" %>
<%@ page import="data.tradeOrder.order.CompletedOrder" %><%--
  Created by IntelliJ IDEA.
  User: roy12
  Date: 20/07/2021
  Time: 16:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%
    Engine engine = ServletUtils.getEngine(request.getServletContext());
    String symbolFromRequest = request.getParameter(ServletUtils.SYMBOL_PARAM);
    Stock stock = null;
    try {
        synchronized (request.getServletContext()) {
            stock = engine.getStock(symbolFromRequest);
        }
    } catch (InvalidStockSymbol invalidStockSymbol) {
        invalidStockSymbol.printStackTrace();
    }
    assert stock != null;%>
<head>
    <title>RiSE</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/pages/data/stockPage.css">
    <script src="${pageContext.request.contextPath}/pages/resources/jquery-2.0.3.min.js"></script>
    <script src="${pageContext.request.contextPath}/pages/resources/utils.js"></script>
</head>
<header class="header">
<%--    <div class="panel info-header" id="title-header">--%>
        <div style="margin-left: 27px;">
            <p class="title">RiSE </p>
            <p>hello <b><%=ServletUtils.getUserNameFromSession(request)%></b>! What would you like to do today?</p>
        </div>
        <a class="back-btn" href = "${pageContext.request.contextPath}/pages/home.html">Go Back</a>
<%--    </div>--%>
</header>
<body>
    <div class="mainContainer">
        <div class="left-panel">
            <div class="inner-top-panel">
                <span class="top-info">
                    <h2 id="symbol"><%=stock.getSymbol()%></h2>
                    <h3>Company: <%=stock.getCompany()%></h3>
                </span>
                <span class="bottom-info">
                    <h3>Current Price: <label id="curr-price"><%=stock.getPrice()%></label></h3>
                    <h3>Business Cycle: <label id="curr-cycle"><%=stock.getCycle()%></label></h3>
                </span>
            </div>
            <div class="inner-bottom-panel" id="role-options">

            <%
                boolean isTrader = ServletUtils.getUserRoleFromSession(request).equalsIgnoreCase("trader");
            if (isTrader) {%>
                <p class="info-header">Possession: <label id="stock-possession">0</label></p>
                <details class="info-header">
                    <summary>Send New Trade Order</summary>
                    <form id="trade-order" method="post" action="trade">
                        <div class="detail-form">
                            <p>Trade Type:</p><select name="tradeType" id="tradeType"
                                                      onchange="disablePriceOnMkt();" onfocus="this.selectedIndex = -1;">
                            <option value="lmt">LMT</option>
                            <option value="mkt">MKT</option>
                            <option value="fok">FOK</option>
                            <option value="ioc">IOC</option>
                        </select>
                        </div>
                        <div class="detail-form">
                            <p>Action: </p><select name="tradeAction" id="trade-action">
                            <option value="sell">Sell</option>
                            <option value="buy">Buy</option>
                        </select>
                        </div>
                        <div class="detail-form">
                            <p>Symbol: </p>
                            <input type="text" id="tradeSymbol" name="tradeSymbol" value="<%=stock.getSymbol()%>" disabled>
                        </div>
                        <div class="detail-form">
                            <p>Amount Of Shares: </p><input type="number" name="tradeAmount" id="tradeAmount" min="1">
                        </div>
                        <div class="detail-form">
                            <p>Price per Share: </p><input type="number" name="tradePrice" id="tradePrice" min="1">
                        </div>
                        <div class="detail-form">
                            <input type="submit" value="Send Order">
                        </div>
                    </form>
                </details>
            <%} else{%>
                <div class="tab">
                    <label style="margin-right: 5px">Open Trades:</label>
                    <button class="tab-link" onclick="openList('sellers-tab')">Sellers</button>
                    <button class="tab-link" onclick="openList('buyers-tab')">Buyers</button>
                </div>
                <div id="sellers-tab" class="tab-content">
                    <ol id="sellers-list">
                    </ol>
                </div>
                <div id="buyers-tab" class="tab-content">
                    <ol id="buyers-list">
                    </ol>
                </div>
            <%}%>
        </div>
        </div>
        <div class="completed-trades right-panel">
            <h2>Completed Trades</h2>
            <div>
                <ol id="comp-trades-list">
                </ol>
            </div>
        </div>
    </div>
    <%String script="data/adminData.js";
        if(isTrader){
        script = "data/traderData.js";
    }%>
<script src="data/live.js"></script>
<script src="<%=script%>"></script>
</body>
</html>
