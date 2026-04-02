package com.vn.shopping.service;

import com.vn.shopping.domain.*;
import com.vn.shopping.domain.response.ResDashboardStaffDTO;
import com.vn.shopping.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardStaffService {

    @Autowired
    private DonHangRepository donHangRepository;

    @Autowired
    private NhanVienRepository nhanVienRepository;

    @Autowired
    private ChiTietDonHangRepository chiTietDonHangRepository;

    @Autowired
    private LichLamViecRepository lichLamViecRepository;

    @Autowired
    private LuongCobanRepository luongCobanRepository;

    @Autowired
    private LuongThuongRepository luongThuongRepository;

    @Autowired
    private DoiHangRepository doiHangRepository;

    @Autowired
    private TraHangRepository traHangRepository;

    public ResDashboardStaffDTO.DashboardSummary getDashboardSummary(Long employeeId) {
        NhanVien employee = nhanVienRepository.findById(employeeId).orElse(null);
        if (employee == null) {
            return null;
        }

        List<DonHang> employeeOrders = donHangRepository.findAll().stream()
                .filter(order -> order.getNhanVien() != null && order.getNhanVien().getId().equals(employeeId))
                .collect(Collectors.toList());

        long ordersNeedProcess = employeeOrders.stream()
                .filter(order -> order.getTrangThai() == null || order.getTrangThai() == 0 || order.getTrangThai() == 1)
                .count();

        long ordersPacking = employeeOrders.stream()
                .filter(order -> order.getTrangThai() != null && order.getTrangThai() == 2)
                .count();

        long ordersShipping = employeeOrders.stream()
                .filter(order -> order.getTrangThai() != null && order.getTrangThai() == 3)
                .count();

        long ordersReturnExchange = 0;
        ordersReturnExchange += doiHangRepository.findAll().stream()
                .filter(item -> item.getDonHang() != null
                        && item.getDonHang().getNhanVien() != null
                        && item.getDonHang().getNhanVien().getId().equals(employeeId)
                        && (item.getTrangThai() == null || item.getTrangThai() == 0))
                .count();
        ordersReturnExchange += traHangRepository.findAll().stream()
                .filter(item -> item.getDonHang() != null
                        && item.getDonHang().getNhanVien() != null
                        && item.getDonHang().getNhanVien().getId().equals(employeeId)
                        && (item.getTrangThai() == null || item.getTrangThai() == 0))
                .count();

        return new ResDashboardStaffDTO.DashboardSummary(
                employeeId,
                employee.getTenNhanVien(),
                employee.getCuaHang() != null ? employee.getCuaHang().getTenCuaHang() : "N/A",
                ordersNeedProcess,
                ordersPacking,
                ordersShipping,
                ordersReturnExchange);
    }

    /**
     * Get staff overview dashboard
     */
    public ResDashboardStaffDTO.StaffOverview getStaffOverview(Long employeeId) {
        NhanVien employee = nhanVienRepository.findById(employeeId).orElse(null);
        if (employee == null) {
            return null;
        }

        ResDashboardStaffDTO.StaffOverview overview = new ResDashboardStaffDTO.StaffOverview();
        overview.setEmployeeId(employeeId);
        overview.setEmployeeName(employee.getTenNhanVien());
        overview.setStoreName(employee.getCuaHang() != null ? employee.getCuaHang().getTenCuaHang() : "N/A");

        // Today's stats
        List<DonHang> todayOrders = donHangRepository.findAll().stream()
                .filter(o -> o.getNhanVien() != null && o.getNhanVien().getId().equals(employeeId)
                        && o.getNgayTao().toLocalDate().equals(LocalDate.now()))
                .collect(Collectors.toList());

        double salesToday = todayOrders.stream()
                .mapToDouble(order -> {
                    List<ChiTietDonHang> details = chiTietDonHangRepository.findByDonHang(order);
                    return details.stream().mapToDouble(this::calculateLineAmount).sum();
                })
                .sum();

        overview.setOrdersHandledToday((long) todayOrders.size());
        overview.setSalesValueToday(salesToday);
        overview.setCustomersServedToday(todayOrders.stream()
                .map(o -> o.getKhachHang() != null ? o.getKhachHang().getId() : null)
                .filter(Objects::nonNull)
                .distinct()
                .count());

        // This month's stats
        LocalDate monthStart = LocalDate.now().withDayOfMonth(1);
        List<DonHang> monthOrders = donHangRepository.findAll().stream()
                .filter(o -> o.getNhanVien() != null && o.getNhanVien().getId().equals(employeeId)
                        && o.getNgayTao().toLocalDate().compareTo(monthStart) >= 0)
                .collect(Collectors.toList());

        double salesMonth = monthOrders.stream()
                .mapToDouble(order -> {
                    List<ChiTietDonHang> details = chiTietDonHangRepository.findByDonHang(order);
                    return details.stream().mapToDouble(this::calculateLineAmount).sum();
                })
                .sum();

        overview.setOrdersHandledThisMonth((long) monthOrders.size());
        overview.setSalesValueThisMonth(salesMonth);

        if (monthOrders.size() > 0) {
            overview.setAverageOrderValue(salesMonth / monthOrders.size());
        }

        return overview;
    }

    /**
     * Get staff's schedule
     */
    public List<ResDashboardStaffDTO.ScheduleInfo> getStaffSchedule(Long employeeId, Integer daysAhead) {
        if (daysAhead == null) {
            daysAhead = 30;
        }

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(daysAhead);

        List<LichLamViec> schedules = lichLamViecRepository.findAll().stream()
                .filter(s -> s.getNhanVien() != null && s.getNhanVien().getId().equals(employeeId)
                        && s.getNgayLamViec().compareTo(startDate) >= 0
                        && s.getNgayLamViec().compareTo(endDate) <= 0)
                .collect(Collectors.toList());

        return schedules.stream()
                .map(schedule -> {
                    ResDashboardStaffDTO.ScheduleInfo info = new ResDashboardStaffDTO.ScheduleInfo();
                    info.setScheduleId(schedule.getId());
                    info.setWorkDate(schedule.getNgayLamViec());
                    info.setStatus(schedule.getTrangThai());

                    // Get shift info from ChiTietLichLam
                    List<ChiTietLichLam> details = schedule.getChiTiets();
                    if (details != null && !details.isEmpty()) {
                        ChiTietLichLam detail = details.get(0);
                        if (detail.getCaLamViec() != null) {
                            info.setShiftName(detail.getCaLamViec().getTenCaLam());
                            info.setShiftTime(detail.getCaLamViec().getGioBatDau() + " - "
                                    + detail.getCaLamViec().getGioKetThuc());
                        }
                    }

                    info.setNote(schedule.getJson());
                    return info;
                })
                .sorted(Comparator.comparing(ResDashboardStaffDTO.ScheduleInfo::getWorkDate))
                .collect(Collectors.toList());
    }

    /**
     * Get staff's sales performance by day/week/month
     */
    public List<ResDashboardStaffDTO.SalesPerformance> getStaffSalesPerformance(Long employeeId, String period) {
        List<ResDashboardStaffDTO.SalesPerformance> performances = new ArrayList<>();

        LocalDate startDate = getStartDate(period);
        LocalDate endDate = LocalDate.now();

        // Group orders by date
        Map<LocalDate, List<DonHang>> ordersByDate = donHangRepository.findAll().stream()
                .filter(o -> o.getNhanVien() != null && o.getNhanVien().getId().equals(employeeId)
                        && o.getNgayTao().toLocalDate().compareTo(startDate) >= 0
                        && o.getNgayTao().toLocalDate().compareTo(endDate) <= 0)
                .collect(Collectors.groupingBy(o -> o.getNgayTao().toLocalDate()));

        ordersByDate.forEach((date, orders) -> {
            double revenue = orders.stream()
                    .mapToDouble(order -> {
                        List<ChiTietDonHang> details = chiTietDonHangRepository.findByDonHang(order);
                        return details.stream().mapToDouble(this::calculateLineAmount).sum();
                    })
                    .sum();

            double commission = revenue * 0.05; // 5% commission on sales

            ResDashboardStaffDTO.SalesPerformance perf = new ResDashboardStaffDTO.SalesPerformance();
            perf.setDate(date);
            perf.setOrdersCount((long) orders.size());
            perf.setSalesRevenue(revenue);
            perf.setCommission(commission);
            perf.setAverageOrderValue(orders.size() > 0 ? revenue / orders.size() : 0);

            performances.add(perf);
        });

        return performances.stream()
                .sorted(Comparator.comparing(ResDashboardStaffDTO.SalesPerformance::getDate))
                .collect(Collectors.toList());
    }

    /**
     * Get staff's salary information
     */
    public List<ResDashboardStaffDTO.SalaryInfo> getStaffSalaryInfo(Long employeeId, Integer months) {
        if (months == null) {
            months = 12;
        }

        List<ResDashboardStaffDTO.SalaryInfo> salaryInfos = new ArrayList<>();

        // Get base salary
        Optional<LuongCoban> baseSalary = luongCobanRepository.findAll().stream()
                .filter(l -> l.getNhanVien() != null && l.getNhanVien().getId().equals(employeeId))
                .max(Comparator.comparing(LuongCoban::getNgayApDung));

        // Get bonus/commission for past months
        for (int i = 0; i < months; i++) {
            YearMonth month = YearMonth.now().minusMonths(i);
            LocalDate monthStart = month.atDay(1);

            ResDashboardStaffDTO.SalaryInfo info = new ResDashboardStaffDTO.SalaryInfo();
            info.setMonth(month.toString());

            if (baseSalary.isPresent()) {
                info.setBaseSalary(baseSalary.get().getLuongCoBan().doubleValue());
            }

            // Calculate commission from sales
            double commission = calculateMonthlyCommission(employeeId, monthStart);
            info.setCommission(commission);

            // Get bonus
            Optional<LuongThuong> bonus = luongThuongRepository.findAll().stream()
                    .filter(l -> l.getNhanVien() != null && l.getNhanVien().getId().equals(employeeId)
                            && l.getNgayBatDau() != null
                            && YearMonth.from(l.getNgayBatDau().toLocalDate()).equals(YearMonth.from(monthStart)))
                    .findFirst();
            if (bonus.isPresent()) {
                info.setBonus(bonus.get().getTienThuong() != null ? bonus.get().getTienThuong().doubleValue() : 0.0);
            } else {
                info.setBonus(0.0);
            }

            double totalSalary = (baseSalary.isPresent() ? baseSalary.get().getLuongCoBan().doubleValue() : 0)
                    + info.getBonus() + commission;
            info.setTotalSalary(totalSalary);
            info.setStatus("Pending"); // Can be enhanced with actual status from database

            salaryInfos.add(info);
        }

        return salaryInfos;
    }

    /**
     * Get pending orders/tasks for staff
     */
    public List<ResDashboardStaffDTO.OrderTask> getStaffPendingOrders(Long employeeId) {
        return donHangRepository.findAll().stream()
                .filter(o -> o.getNhanVien() != null && o.getNhanVien().getId().equals(employeeId)
                        && (o.getTrangThai() == null || o.getTrangThai() < 3)) // status < 3 = not completed
                .map(order -> {
                    ResDashboardStaffDTO.OrderTask task = new ResDashboardStaffDTO.OrderTask();
                    task.setOrderId(order.getId());
                    if (order.getKhachHang() != null) {
                        task.setCustomerName(order.getKhachHang().getTenKhachHang());
                        task.setCustomerPhone(order.getKhachHang().getSdt());
                    }

                    List<ChiTietDonHang> details = chiTietDonHangRepository.findByDonHang(order);
                    double total = details.stream()
                            .mapToDouble(this::calculateLineAmount)
                            .sum();
                    task.setOrderTotal(total);
                    task.setOrderStatus(order.getTrangThai());
                    task.setCreatedTime(order.getNgayTao() != null ? order.getNgayTao().toString() : "N/A");

                    return task;
                })
                .sorted(Comparator.comparing(ResDashboardStaffDTO.OrderTask::getCreatedTime).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Get weekly performance summary
     */
    public ResDashboardStaffDTO.WeekPerformance getWeeklyPerformance(Long employeeId) {
        LocalDate weekStart = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1);
        LocalDate weekEnd = weekStart.plusDays(6);

        List<DonHang> weekOrders = donHangRepository.findAll().stream()
                .filter(o -> o.getNhanVien() != null && o.getNhanVien().getId().equals(employeeId)
                        && o.getNgayTao().toLocalDate().compareTo(weekStart) >= 0
                        && o.getNgayTao().toLocalDate().compareTo(weekEnd) <= 0)
                .collect(Collectors.toList());

        double totalRevenue = weekOrders.stream()
                .mapToDouble(order -> {
                    List<ChiTietDonHang> details = chiTietDonHangRepository.findByDonHang(order);
                    return details.stream().mapToDouble(this::calculateLineAmount).sum();
                })
                .sum();

        long customersServed = weekOrders.stream()
                .map(o -> o.getKhachHang() != null ? o.getKhachHang().getId() : null)
                .filter(Objects::nonNull)
                .distinct()
                .count();

        ResDashboardStaffDTO.WeekPerformance perf = new ResDashboardStaffDTO.WeekPerformance();
        perf.setWeekRange(weekStart + " to " + weekEnd);
        perf.setTotalOrders((long) weekOrders.size());
        perf.setTotalRevenue(totalRevenue);
        perf.setAverageDailyRevenue(totalRevenue / 7);
        perf.setCustomersServed(customersServed);
        perf.setAverageOrderValue(weekOrders.size() > 0 ? totalRevenue / weekOrders.size() : 0);

        return perf;
    }

    /**
     * Get upcoming schedule
     */
    public List<ResDashboardStaffDTO.UpcomingSchedule> getUpcomingSchedule(Long employeeId, Integer daysAhead) {
        if (daysAhead == null) {
            daysAhead = 7;
        }

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(daysAhead);

        return lichLamViecRepository.findAll().stream()
                .filter(s -> s.getNhanVien() != null && s.getNhanVien().getId().equals(employeeId)
                        && s.getNgayLamViec().compareTo(startDate) >= 0
                        && s.getNgayLamViec().compareTo(endDate) <= 0)
                .map(schedule -> {
                    ResDashboardStaffDTO.UpcomingSchedule upcoming = new ResDashboardStaffDTO.UpcomingSchedule();
                    upcoming.setDate(schedule.getNgayLamViec());
                    upcoming.setStatus(schedule.getTrangThai());

                    List<ChiTietLichLam> details = schedule.getChiTiets();
                    if (details != null && !details.isEmpty()) {
                        ChiTietLichLam detail = details.get(0);
                        if (detail.getCaLamViec() != null) {
                            upcoming.setShiftName(detail.getCaLamViec().getTenCaLam());
                            upcoming.setShiftTime(detail.getCaLamViec().getGioBatDau() + " - "
                                    + detail.getCaLamViec().getGioKetThuc());
                        }
                    }

                    return upcoming;
                })
                .sorted(Comparator.comparing(ResDashboardStaffDTO.UpcomingSchedule::getDate))
                .collect(Collectors.toList());
    }

    // ===== Helper Methods =====

    private LocalDate getStartDate(String period) {
        LocalDate today = LocalDate.now();
        switch (period.toLowerCase()) {
            case "week":
                return today.minusWeeks(1);
            case "month":
                return today.minusMonths(1);
            case "quarter":
                return today.minusMonths(3);
            case "year":
                return today.minusYears(1);
            default:
                return today;
        }
    }

    private double calculateMonthlyCommission(Long employeeId, LocalDate monthStart) {
        LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);

        List<DonHang> monthOrders = donHangRepository.findAll().stream()
                .filter(o -> o.getNhanVien() != null && o.getNhanVien().getId().equals(employeeId)
                        && o.getNgayTao().toLocalDate().compareTo(monthStart) >= 0
                        && o.getNgayTao().toLocalDate().compareTo(monthEnd) <= 0)
                .collect(Collectors.toList());

        double totalRevenue = monthOrders.stream()
                .mapToDouble(order -> {
                    List<ChiTietDonHang> details = chiTietDonHangRepository.findByDonHang(order);
                    return details.stream().mapToDouble(this::calculateLineAmount).sum();
                })
                .sum();

        return totalRevenue * 0.05; // 5% commission
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
}
