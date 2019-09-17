/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qz;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author ares
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class PrintPageSetting {
    private Double width;
    private Double height;
    private Double leftMargin;
    private Double rightMargin;
    private Double topMargin;
    private Double bottomMargin;

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }
    
    

    public Double getLeftMargin() {
        return leftMargin;
    }

    public void setLeftMargin(Double leftMargin) {
        this.leftMargin = leftMargin;
    }

    public Double getRightMargin() {
        return rightMargin;
    }

    public void setRightMargin(Double rightMargin) {
        this.rightMargin = rightMargin;
    }

    public Double getTopMargin() {
        return topMargin;
    }

    public void setTopMargin(Double topMargin) {
        this.topMargin = topMargin;
    }

    public Double getBottomMargin() {
        return bottomMargin;
    }

    public void setBottomMargin(Double bottomMargin) {
        this.bottomMargin = bottomMargin;
    }

}
