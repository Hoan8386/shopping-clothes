package com.vn.shopping.domain.response;

import com.vn.shopping.domain.KhuyenMaiTheoDiem;
import com.vn.shopping.domain.KhuyenMaiTheoHoaDon;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResKhuyenMaiHopLeDTO {
    private List<KhuyenMaiTheoHoaDon> khuyenMaiHoaDon;
    private List<KhuyenMaiTheoDiem> khuyenMaiDiem;
}
