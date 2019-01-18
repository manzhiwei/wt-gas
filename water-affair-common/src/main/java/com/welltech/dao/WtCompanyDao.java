package com.welltech.dao;

import com.welltech.entity.WtCompany;

public interface WtCompanyDao {

    WtCompany findCompanyById(Integer companyId);
}
