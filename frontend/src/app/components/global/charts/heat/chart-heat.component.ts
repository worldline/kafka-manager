import { Component, Input, Output, EventEmitter } from '@angular/core';
import { AbstractChartComponent } from '../parent/chart-abstract.component';
import { TranslateService } from '@ngx-translate/core';
import * as uuid from 'uuid';
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from 'ngx-toastr';

@Component({
    selector: 'app-chart-heat',
    templateUrl: '../parent/chart-abstract.component.html',
    styleUrls: ['../parent/chart-abstract.component.scss'],
})
export class ChartHeatComponent extends AbstractChartComponent {

    @Input() displayRefreshButton: boolean = true;
    @Output() refresh = new EventEmitter<void>();
    @Output() yClick = new EventEmitter<any>();

    constructor(protected translate: TranslateService,
                protected toastr: ToastrService,
                protected spinner: NgxSpinnerService) { 
        super(translate, toastr, spinner);
    }

    createChart(am4core, am4charts, am4themes_animated) {
        // Chart code goes here
        am4core.useTheme(am4themes_animated);
        // Themes end

        // Create chart instance
        let chart = am4core.create(this.divId, am4charts.XYChart);
        chart.maskBullets = false;

        // Add data
        chart.data = this.mapData(am4core, this.data);

        // Update height
        this.updateHeight();

        // Create axes
        let xAxis = chart.xAxes.push(new am4charts.CategoryAxis());
        xAxis.dataFields.category = this.data.headers.x.column
        xAxis.renderer.grid.template.disabled = true;
        xAxis.renderer.minGridDistance = 40;
        xAxis.dateFormatter = new am4core.DateFormatter();
        xAxis.dateFormatter.dateFormat = "MM-dd";

        let yAxis = chart.yAxes.push(new am4charts.CategoryAxis());
        yAxis.renderer.grid.template.disabled = true;
        yAxis.renderer.inversed = true;
        yAxis.renderer.minGridDistance = 30;
        yAxis.dataFields.category = this.data.headers.y.column;
        yAxis.renderer.labels.template.events.on("hit", (event) => {
            this.yClick.emit(event.target.dataItem.dataContext);
        })

        // Create series
        let series = chart.series.push(new am4charts.ColumnSeries());
        series.dataFields.categoryX = this.data.headers.x.column;
        series.dataFields.categoryY = this.data.headers.y.column;
        series.dataFields.value = "value";
        series.sequencedInterpolation = true;
        series.defaultState.transitionDuration = 3000;

        let bgColor = new am4core.InterfaceColorSet().getFor("background");

        let columnTemplate = series.columns.template;
        columnTemplate.strokeWidth = 1;
        columnTemplate.strokeOpacity = 0.2;
        columnTemplate.stroke = bgColor;
        columnTemplate.tooltipText = "Lag {value.workingValue.formatNumber('#.')}";
        columnTemplate.width = am4core.percent(100);
        columnTemplate.height = am4core.percent(100);

        series.heatRules.push({
            target: columnTemplate,
            property: "fill",
            min: am4core.color(bgColor),
            max: chart.colors.getIndex(0)
        });

        // heat legend
        let heatLegend = chart.bottomAxesContainer.createChild(am4charts.HeatLegend);
        heatLegend.width = am4core.percent(100);
        heatLegend.series = series;
        heatLegend.valueAxis.renderer.labels.template.fontSize = 9;
        heatLegend.valueAxis.renderer.minGridDistance = 30;
        

        // heat legend behavior
        series.columns.template.events.on("over", function (event) {
            handleHover(event.target);
        })

        series.columns.template.events.on("hit", function (event) {
            handleHover(event.target);
        })

        function handleHover(column) {
            if (!isNaN(column.dataItem.value)) {
                heatLegend.valueAxis.showTooltipAt(column.dataItem.value)
            }
            else {
                heatLegend.valueAxis.hideTooltip();
            }
        }

        series.columns.template.events.on("out", function (event) {
            heatLegend.valueAxis.hideTooltip();
        })

        // Export menu
        this.createMenu(am4core, chart);

        return chart;
    }

    updateHeight() {
        const yType = this.data.headers.y.column;
        const uniqueTypes = this.data.data.map(d => d[yType]).filter((value, index, self) => self.indexOf(value) === index);
        this.heightStyle = (50 * uniqueTypes.length + 100) + 'px';
    }

    mapData(am4core, data) {
        return data.data;
    }

    updateSeries(am4core, am4charts, chart) {
        this.updateHeight();
    }

}
