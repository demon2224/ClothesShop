<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Thống kê doanh thu</title>
        <meta content="width=device-width, initial-scale=1.0" name="viewport">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/boxicons@latest/css/boxicons.min.css">
        <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.2/css/all.css">
        <link rel="stylesheet" type="text/css" href="assets/admin/css/main.css">
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    </head>
    <body class="app sidebar-mini rtl">
        <%@include file="/WEB-INF/include/sidebar.jsp"%>

        <main class="app-content">
            <div class="row">
                <!-- Biểu đồ cột (Monthly) -->
                <div class="col-md-6">
                    <div class="tile">
                        <h3 class="tile-title">Doanh thu theo tháng</h3>
                        <div class="tile-body">
                            <canvas id="monthlyChart"></canvas>
                        </div>
                    </div>
                </div>

                <!-- Biểu đồ đường (Yearly) -->
                <div class="col-md-6">
                    <div class="tile">
                        <h3 class="tile-title">Doanh thu theo năm</h3>
                        <div class="tile-body">
                            <canvas id="yearlyChart"></canvas>
                        </div>
                    </div>
                </div>

                <!-- Biểu đồ tròn (Orders) -->
                <div class="col-md-12">
                    <div class="tile">
                        <h3 class="tile-title">
                            Số lượng đơn hàng</h3>
                        <div class="tile-body" style="display: flex; justify-content: center;">
                            <canvas id="productPieChart" style="max-width: 1200px; max-height: 600px;"></canvas>
                        </div>
                    </div>
                </div>
            </div>
        </main>

        <script>
            // Biểu đồ cột theo tháng
            new Chart(document.getElementById("monthlyChart"), {
            type: 'bar',
                    data: {
                    labels: ['T1', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'T8', 'T9', 'T10', 'T11', 'T12'],
                            datasets: [{
                            label: 'Doanh thu (VNĐ)',
                                    data: [
            ${MONTH1}, ${MONTH2}, ${MONTH3}, ${MONTH4},
            ${MONTH5}, ${MONTH6}, ${MONTH7}, ${MONTH8},
            ${MONTH9}, ${MONTH10}, ${MONTH11}, ${MONTH12}
                                    ],
                                    backgroundColor: 'rgba(54, 162, 235, 0.5)'
                            }]
                    },
                    options: {
                    responsive: true,
                            scales: {
                            y: {
                            beginAtZero: true
                            }
                            }
                    }
            });
            // Biểu đồ đường theo năm
            new Chart(document.getElementById("yearlyChart"), {
            type: 'line',
                    data: {
                    labels: ['2020', '2021', '2022', '2023', '2024'],
                            datasets: [{
                            label: 'Doanh thu (VNĐ)',
                                    data: [${YEAR1}, ${YEAR2}, ${YEAR3}, ${YEAR4}, ${YEAR5}],
                                    borderColor: 'rgb(75, 192, 192)',
                                    tension: 0.1
                            }]
                    }
            });
            // Biểu đồ tròn top sản phẩm
            new Chart(document.getElementById("productPieChart"), {
            type: 'pie',
                    data: {
                    labels: [
            <c:forEach items="${TOP_PRODUCTS}" var="product" varStatus="status">
                    '${product[1]}'${!status.last ? ',' : ''}
            </c:forEach>
                    ],
                            datasets: [{
                            data: [
            <c:forEach items="${TOP_PRODUCTS}" var="product" varStatus="status">
                ${product[2]}${!status.last ? ',' : ''}
            </c:forEach>
                            ],
                                    backgroundColor: [
                                            '#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF'
                                    ]
                            }]
                    },
                    options: {
                    responsive: true,
                            plugins: {
                            legend: {
                            position: 'bottom'
                            }
                            }
                    }
            });
        </script>
    </body>
</html>