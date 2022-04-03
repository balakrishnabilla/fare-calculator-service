package com.farecalulator.dao.entity;

import com.farecalulator.dao.Data;
import com.farecalulator.model.PeakSchedule;

import java.util.List;

public class PeakTimeData implements Data {
  private List<PeakSchedule> list = null;

  public PeakTimeData(List<PeakSchedule> list) {
    this.list = list;
  }

  public List<PeakSchedule> getList() {
    return list;
  }

  public void setList(List<PeakSchedule> list) {
    this.list = list;
  }
}
