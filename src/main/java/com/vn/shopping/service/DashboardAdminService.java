package com.vn.shopping.service;

import com.vn.shopping.domain.*;
import com.vn.shopping.domain.response.ResDashboardAdminDTO;
import com.vn.shopping.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardAdminService {

    @Autowired
    private DonHangRepository donHangRepository;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private NhanVienRepository nhanVienRepository;

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private ChiTietDonHangRepository chiTietDonHangRepository;

    @Autowired
    private TraHangRepository traHangRepository;

    @Autowired
    private DoiHangRepository doiHangRepository;

    @Autowired
    private CuaHangRepository cuaHangRepository;

    public ResDashboardAdminDTO.DashboardSummary getDashboardSummary() {
        LocalDate today = LocalDate.now();
        YearMonth currentMonth = YearMonth.now();
        YearMonth previousMonth = currentMonth.minusMonths(1);

        List<DonHang> allOrders = donHangRepository.findAll();
        List<DonHang> currentMonthOrders = allOrders.stream()
                .filter(order -> order.getNgayTao() != null
                        && YearMonth.from(order.getNgayTao()).equals(currentMonth))
                .collect(Collectors.toList());
        List<DonHang> todayOrders = currentMonthOrders.stream()
                .filter(order -> order.getNgayTao().toLocalDate().equals(today))
                .collect(Collectors.toList());
        List<DonHang> previousMonthOrders = allOrders.stream()
                .filter(order -> order.getNgayTao() != null
                        && YearMonth.from(order.getNgayTao()).equals(previousMonth))
                .collect(Collectors.toList());

        double todayRevenue = calculateRevenue(todayOrders);
        double monthRevenue = calculateRevenue(currentMonthOrders);
        double previousMonthRevenue = calculateRevenue(previousMonthOrders);

        long monthlyOrders = currentMonthOrders.size();
        long totalCustomers = khachHangRepository.count();
        long productsSold = currentMonthOrders.stream()
                .flatMap(order -> chiTietDonHangRepository.findByDonHang(order).stream())
                .mapToLong(detail -> detail.getSoLuong() != null ? detail.getSoLuong() : 0)
                .sum();

        long newCustomers = currentMonthOrders.stream()
                .map(DonHang::getKhachHang)
                .filter(Objects::nonNull)
                .map(KhachHang::getId)
                .filter(Objects::nonNull)
                .distinct()
                .filter(customerId -> allOrders.stream()
                        .filter(order -> order.getKhachHang() != null
                                && customerId.equals(order.getKhachHang().getId())
                                && order.getNgayTao() != null)
                        .map(DonHang::getNgayTao)
                        .min(LocalDateTime::compareTo)
                        .map(firstOrderDate -> YearMonth.from(firstOrderDate).equals(currentMonth))
                        .orElse(false))
                .count();

        List<ResDashboardAdminDTO.DailyStat> revenueByDay = aggregateByDay(currentMonthOrders, true);
        List<ResDashboardAdminDTO.DailyStat> ordersByDay = aggregateByDay(currentMonthOrders, false);
        List<ResDashboardAdminDTO.TopProductStat> topProducts = aggregateTopProducts(currentMonthOrders, 10);

        double growthPercent = previousMonthRevenue > 0
                ? ((monthRevenue - previousMonthRevenue) / previousMonthRevenue) * 100
                : 0;

        return new ResDashboardAdminDTO.DashboardSummary(
                todayRevenue,
                monthRevenue,
                previousMonthRevenue,
                growthPercent,
                monthlyOrders,
                newCustomers,
                productsSold,
                totalCustomers,
                revenueByDay,
                ordersByDay,
                topProducts);
    }

    /**
     * Get overall dashboard overview
     */
    public ResDashboardAdminDTO.DashboardOverview getDashboardOverview(String period) {
        ResDashboardAdminDTO.DashboardOverview overview = new ResDashboardAdminDTO.DashboardOverview();

        // Total Revenue & Orders
        List<DonHang> orders = donHangRepository.findAll(); // Implement filter by date in actual code
        double totalRevenue = orders.stream()
                .mapToDouble(order -> {
                    List<ChiTietDonHang> details = chiTietDonHangRepository.findByDonHang(order);
                    return details.stream().mapToDouble(this::calculateLineAmount).sum();
                })
                .sum();

        overview.setTotalRevenue(totalRevenue);
        overview.setTotalOrders((long) orders.size());

        // Total Customers
        overview.setTotalCustomers(khachHangRepository.count());

        // Total Employees
        overview.setTotalEmployees(nhanVienRepository.count());

        // Total Inventory
        long totalInventory = sanPhamRepository.findAll().stream()
                .mapToLong(p -> p.getSoLuong() != null ? p.getSoLuong() : 0)
                .sum();
        overview.setTotalInventory(totalInventory);

        // Average Order Value
        if (orders.size() > 0) {
            overview.setAverageOrderValue(totalRevenue / orders.size());
        }

        // Revenue Growth (comparing with previous period)
        Double previousRevenue = calculatePreviousPeriodRevenue(period);
        if (previousRevenue > 0) {
            double growth = ((totalRevenue - previousRevenue) / previousRevenue) * 100;
            overview.setRevenueGrowth(growth);
        }

        return overview;
    }

    /**
     * Get top selling products
     */
    public List<ResDashboardAdminDTO.TopSellingProduct> getTopSellingProducts(int limit, String period) {
        List<DonHang> orders = donHangRepository.findAll(); // Filter by date

        Map<Long, Long> productQuantity = new HashMap<>();
        Map<Long, Double> productRevenue = new HashMap<>();
        Map<Long, String> productNames = new HashMap<>();

        for (DonHang order : orders) {
            List<ChiTietDonHang> details = chiTietDonHangRepository.findByDonHang(order);
            for (ChiTietDonHang detail : details) {
                if (detail.getChiTietSanPham() != null && detail.getChiTietSanPham().getSanPham() != null) {
                    Long productId = detail.getChiTietSanPham().getSanPham().getId();
                    Long qty = productQuantity.getOrDefault(productId, 0L);
                    productQuantity.put(productId, qty + detail.getSoLuong());

                    Double revenue = productRevenue.getOrDefault(productId, 0.0);
                    productRevenue.put(productId, revenue + calculateLineAmount(detail));

                    productNames.put(productId, detail.getChiTietSanPham().getSanPham().getTenSanPham());
                }
            }
        }

        return productQuantity.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(limit)
                .map(entry -> {
                    ResDashboardAdminDTO.TopSellingProduct product = new ResDashboardAdminDTO.TopSellingProduct();
                    product.setProductId(entry.getKey());
                    product.setProductName(productNames.get(entry.getKey()));
                    product.setQuantitySold(entry.getValue());
                    product.setTotalRevenue(productRevenue.get(entry.getKey()));
                    // Calculate profit margin (example: 30%)
                    product.setProfitMargin(30.0);
                    return product;
                })
                .collect(Collectors.toList());
    }

    /**
     * Get revenue statistics by period
     */
    public List<ResDashboardAdminDTO.RevenueStat> getRevenueStatistics(String period) {
        List<ResDashboardAdminDTO.RevenueStat> stats = new ArrayList<>();
        List<DonHang> orders = donHangRepository.findAll();

        // Group by period (day, week, month based on input)
        Map<String, Double> revenueMap = new HashMap<>();
        Map<String, Long> orderCountMap = new HashMap<>();

        for (DonHang order : orders) {
            String key = getGroupingKey(order.getNgayTao(), period);
            List<ChiTietDonHang> details = chiTietDonHangRepository.findByDonHang(order);
            double revenue = details.stream()
                    .mapToDouble(this::calculateLineAmount)
                    .sum();

            revenueMap.put(key, revenueMap.getOrDefault(key, 0.0) + revenue);
            orderCountMap.put(key, orderCountMap.getOrDefault(key, 0L) + 1);
        }

        revenueMap.forEach((key, revenue) -> {
            ResDashboardAdminDTO.RevenueStat stat = new ResDashboardAdminDTO.RevenueStat();
            stat.setPeriod(key);
            stat.setRevenue(revenue);
            stat.setOrderCount(orderCountMap.get(key));
            stat.setAverageOrderValue(revenue / orderCountMap.get(key));
            stats.add(stat);
        });

        return stats.stream()
                .sorted((a, b) -> a.getPeriod().compareTo(b.getPeriod()))
                .collect(Collectors.toList());
    }

    /**
     * Get customer statistics
     */
    public ResDashboardAdminDTO.CustomerStat getCustomerStatistics(String period) {
        long totalCustomers = khachHangRepository.count();

        // New customers this month (can be enhanced with created_at field)
        long newCustomers = 0;

        List<DonHang> orders = donHangRepository.findAll();
        Set<Long> activeCustomers = orders.stream()
                .map(o -> o.getKhachHang() != null ? o.getKhachHang().getId() : null)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        double averageShoppingValue = 0;
        if (!activeCustomers.isEmpty()) {
            double totalValue = orders.stream()
                    .mapToDouble(order -> {
                        List<ChiTietDonHang> details = chiTietDonHangRepository.findByDonHang(order);
                        return details.stream().mapToDouble(this::calculateLineAmount).sum();
                    })
                    .sum();
            averageShoppingValue = totalValue / activeCustomers.size();
        }

        ResDashboardAdminDTO.CustomerStat stat = new ResDashboardAdminDTO.CustomerStat();
        stat.setTotalCustomers(totalCustomers);
        stat.setNewCustomersThisMonth(newCustomers);
        stat.setActiveCustomers((long) activeCustomers.size());
        stat.setAverageShoppingValue(averageShoppingValue);
        stat.setCustomerRetentionRate(
                activeCustomers.isEmpty() ? 0 : (double) activeCustomers.size() / totalCustomers * 100);

        return stat;
    }

    /**
     * Get employee statistics
     */
    public ResDashboardAdminDTO.EmployeeStat getEmployeeStatistics(String period) {
        long totalEmployees = nhanVienRepository.count();
        long activeEmployees = nhanVienRepository.findAll().stream()
                .filter(e -> e.getTrangThai() == 1) // Assuming 1 = active
                .count();

        List<DonHang> orders = donHangRepository.findAll();
        double totalEmployeeSales = 0;

        for (DonHang order : orders) {
            if (order.getNhanVien() != null) {
                List<ChiTietDonHang> details = chiTietDonHangRepository.findByDonHang(order);
                totalEmployeeSales += details.stream()
                        .mapToDouble(this::calculateLineAmount)
                        .sum();
            }
        }

        double averageSalesPerEmployee = activeEmployees > 0 ? totalEmployeeSales / activeEmployees : 0;

        ResDashboardAdminDTO.EmployeeStat stat = new ResDashboardAdminDTO.EmployeeStat();
        stat.setTotalEmployees(totalEmployees);
        stat.setActiveEmployees(activeEmployees);
        stat.setEmployeesOnLeave(totalEmployees - activeEmployees);
        stat.setAverageSalesPerEmployee(averageSalesPerEmployee);
        stat.setTopEmployeesSalesValue(totalEmployeeSales);

        return stat;
    }

    /**
     * Get store performance
     */
    public List<ResDashboardAdminDTO.StorePerformance> getStorePerformance(String period) {
        List<CuaHang> stores = cuaHangRepository.findAll();
        List<ResDashboardAdminDTO.StorePerformance> performances = new ArrayList<>();

        for (CuaHang store : stores) {
            List<DonHang> storeOrders = donHangRepository.findAll().stream()
                    .filter(o -> o.getCuaHang() != null && o.getCuaHang().getId().equals(store.getId()))
                    .collect(Collectors.toList());

            double totalSales = storeOrders.stream()
                    .mapToDouble(order -> {
                        List<ChiTietDonHang> details = chiTietDonHangRepository.findByDonHang(order);
                        return details.stream().mapToDouble(this::calculateLineAmount).sum();
                    })
                    .sum();

            long employees = nhanVienRepository.findAll().stream()
                    .filter(e -> e.getCuaHang() != null && e.getCuaHang().getId().equals(store.getId()))
                    .count();

            ResDashboardAdminDTO.StorePerformance perf = new ResDashboardAdminDTO.StorePerformance();
            perf.setStoreId(store.getId());
            perf.setStoreName(store.getTenCuaHang());
            perf.setTotalSales(totalSales);
            perf.setOrderCount((long) storeOrders.size());
            perf.setEmployeeCount(employees);

            performances.add(perf);
        }

        return performances;
    }

    /**
     * Get return and exchange statistics
     */
    public ResDashboardAdminDTO.ReturnExchangeStat getReturnExchangeStatistics(String period) {
        long totalReturns = traHangRepository.count();
        long totalExchanges = doiHangRepository.count();

        List<DonHang> allOrders = donHangRepository.findAll();
        long totalOrders = allOrders.size();

        double returnRate = totalOrders > 0 ? (double) totalReturns / totalOrders * 100 : 0;
        double exchangeRate = totalOrders > 0 ? (double) totalExchanges / totalOrders * 100 : 0;

        double totalReturnAmount = 0; // Can be calculated from traHang details

        ResDashboardAdminDTO.ReturnExchangeStat stat = new ResDashboardAdminDTO.ReturnExchangeStat();
        stat.setTotalReturns(totalReturns);
        stat.setTotalExchanges(totalExchanges);
        stat.setReturnRate(returnRate);
        stat.setExchangeRate(exchangeRate);
        stat.setTotalReturnAmount(totalReturnAmount);

        return stat;
    }

    // ===== Helper Methods =====

    private LocalDateTime getStartDate(String period) {
        LocalDate today = LocalDate.now();
        switch (period.toLowerCase()) {
            case "week":
                return today.minusWeeks(1).atStartOfDay();
            case "month":
                return today.minusMonths(1).atStartOfDay();
            case "quarter":
                return today.minusMonths(3).atStartOfDay();
            case "year":
                return today.minusYears(1).atStartOfDay();
            default:
                return today.atStartOfDay();
        }
    }

    private Double calculatePreviousPeriodRevenue(String period) {
        LocalDateTime currentPeriodStart = getStartDate(period);
        LocalDateTime previousPeriodStart = getStartDate(period).minusDays(1);

        List<DonHang> orders = donHangRepository.findAll().stream()
                .filter(order -> order.getNgayTao() != null
                        && order.getNgayTao().isAfter(previousPeriodStart)
                        && order.getNgayTao().isBefore(currentPeriodStart))
                .collect(Collectors.toList());

        return orders.stream()
                .mapToDouble(order -> {
                    List<ChiTietDonHang> details = chiTietDonHangRepository.findByDonHang(order);
                    return details.stream().mapToDouble(this::calculateLineAmount).sum();
                })
                .sum();
    }

    private double calculateLineAmount(ChiTietDonHang detail) {
        if (detail.getThanhTien() != null) {
            return detail.getThanhTien();
        }

        double unitPrice = detail.getGiaGiam() != null
                ? detail.getGiaGiam()
                : (detail.getGiaSanPham() != null ? detail.getGiaSanPham() : 0.0);
        int quantity = detail.getSoLuong() != null ? detail.getSoLuong() : 0;
        return unitPrice * quantity;
    }

    private double calculateRevenue(List<DonHang> orders) {
        return orders.stream()
                .mapToDouble(order -> chiTietDonHangRepository.findByDonHang(order).stream()
                        .mapToDouble(this::calculateLineAmount)
                        .sum())
                .sum();
    }

    private List<ResDashboardAdminDTO.DailyStat> aggregateByDay(List<DonHang> orders, boolean revenueMode) {
        Map<LocalDate, List<DonHang>> grouped = orders.stream()
                .filter(order -> order.getNgayTao() != null)
                .collect(Collectors.groupingBy(order -> order.getNgayTao().toLocalDate()));

        return grouped.entrySet().stream()
                .map(entry -> {
                    double revenue = calculateRevenue(entry.getValue());
                    long orderCount = entry.getValue().size();
                    return new ResDashboardAdminDTO.DailyStat(
                            entry.getKey().toString(),
                            revenueMode ? revenue : 0.0,
                            revenueMode ? orderCount : orderCount);
                })
                .sorted(Comparator.comparing(ResDashboardAdminDTO.DailyStat::getDate))
                .collect(Collectors.toList());
    }

    private List<ResDashboardAdminDTO.TopProductStat> aggregateTopProducts(List<DonHang> orders, int limit) {
        Map<Long, Long> quantityMap = new HashMap<>();
        Map<Long, Double> revenueMap = new HashMap<>();
        Map<Long, String> nameMap = new HashMap<>();

        for (DonHang order : orders) {
            for (ChiTietDonHang detail : chiTietDonHangRepository.findByDonHang(order)) {
                if (detail.getChiTietSanPham() != null && detail.getChiTietSanPham().getSanPham() != null) {
                    Long productId = detail.getChiTietSanPham().getSanPham().getId();
                    quantityMap.put(productId, quantityMap.getOrDefault(productId, 0L)
                            + (detail.getSoLuong() != null ? detail.getSoLuong() : 0));
                    revenueMap.put(productId, revenueMap.getOrDefault(productId, 0.0) + calculateLineAmount(detail));
                    nameMap.put(productId, detail.getChiTietSanPham().getSanPham().getTenSanPham());
                }
            }
        }

        return quantityMap.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(limit)
                .map(entry -> new ResDashboardAdminDTO.TopProductStat(
                        entry.getKey(),
                        nameMap.get(entry.getKey()),
                        entry.getValue(),
                        revenueMap.getOrDefault(entry.getKey(), 0.0),
                        30.0))
                .collect(Collectors.toList());
    }

    private String getGroupingKey(LocalDateTime dateTime, String period) {
        if (dateTime == null)
            return "Unknown";
        switch (period.toLowerCase()) {
            case "day":
                return dateTime.toLocalDate().toString();
            case "week":
                return "Week " + dateTime.get(WeekFields.ISO.weekOfWeekBasedYear());
            case "month":
                return dateTime.getMonth().toString() + " " + dateTime.getYear();
            case "year":
                return String.valueOf(dateTime.getYear());
            default:
                return dateTime.toLocalDate().toString();
        }
    }
}
