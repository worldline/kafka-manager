import { Component, Input, Output, EventEmitter } from '@angular/core';
import { AbstractChartComponent } from '../parent/chart-abstract.component';
import { TranslateService } from '@ngx-translate/core';
import * as uuid from 'uuid';
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from 'ngx-toastr';

@Component({
    selector: 'app-chart-line',
    templateUrl: '../parent/chart-abstract.component.html',
    styleUrls: ['../parent/chart-abstract.component.scss'],
})
export class ChartLineComponent extends AbstractChartComponent {

    @Input() displayRefreshButton: boolean = true;
    @Input() interval: string = "minute";
    @Input() displayScrollbarX: boolean = true;
    @Input() displayScrollbarY: boolean = true;
    @Output() refresh = new EventEmitter<void>();

    constructor(protected translate: TranslateService,
                protected toastr: ToastrService,
                protected spinner: NgxSpinnerService) {
        super(translate, toastr, spinner);
    }

    createChart(am4core, am4charts, am4themes_animated) {
        // Chart code goes here
        am4core.useTheme(am4themes_animated);
        // Themes end

        if (!this.data) {
            return;
        }

        // Create chart instance
        let chart = am4core.create(this.divId, am4charts.XYChart);

        // Add data
        chart.data = this.mapData(am4core, this.data);

        // Create axes
        let dateAxis = chart.xAxes.push(new am4charts.DateAxis());
        dateAxis.baseInterval = {
            "timeUnit": this.interval,
            "count": 1
        };
        chart.yAxes.push(new am4charts.ValueAxis());

        // Create vertical scrollbar and place it before the value axis
        if (this.displayScrollbarY) {
            chart.scrollbarY = new am4core.Scrollbar();
            chart.scrollbarY.parent = chart.leftAxesContainer;
            chart.scrollbarY.toBack();
        }

        // Create a horizontal scrollbar with previe and place it underneath the date axis
        let scrollbarX = new am4charts.XYChartScrollbar();
        if (this.displayScrollbarX) {
            chart.scrollbarX = scrollbarX
            chart.scrollbarX.parent = chart.bottomAxesContainer;
            chart.scrollbarX.scrollbarChart.plotContainer.visible = false;
        }

        // Add refresh button
        if (this.displayRefreshButton && this.refresh.observers.length > 0) {
            let button = chart.chartContainer.createChild(am4core.Button);
            button.label.text = this.translate.instant('metrics.charts.refresh');
            button.padding(5, 5, 5, 5);
            button.width = 90;
            button.align = "right";
            button.marginRight = 15;
            button.events.on("hit", () => {
                this.refresh.emit();
            });
        }

        // Add cursor
        chart.cursor = new am4charts.XYCursor();
        chart.cursor.xAxis = dateAxis;

        // Create series
        this.createSerieList(am4core, am4charts, chart);

        // Legend
        chart.legend = new am4charts.Legend();

        // Export menu
        this.createMenu(am4core, chart);

        return chart;
    }

    mapData(am4core, data) {
        return data.data;
    }

    updateSeries(am4core, am4charts, chart) {
        chart.series.clear();
        this.createSerieList(am4core, am4charts, chart);
    }

    createSerieList(am4core, am4charts, chart) {
        // Create series
        const seriesList = [];
        this.data.headers.forEach(header => {
            let series = this.createLineSerie(am4core, am4charts, header.column, header.label);
            seriesList.push(chart.series.push(series));
            chart.scrollbarX.series.push(series);
        });

        // Update cursor
        chart.cursor.snapToSeries = seriesList;
    }

    createLineSerie(am4core, am4charts, valueY, intlLabel) {
        let series = new am4charts.LineSeries();
        series.dataFields.valueY = valueY;
        series.dataFields.dateX = "date";
        series.strokeWidth = 2;
        series.minBulletDistance = 10;
        series.tooltipText = "{valueY}";
        series.tooltip.pointerOrientation = "vertical";
        series.tooltip.background.cornerRadius = 20;
        series.tooltip.background.fillOpacity = 0.5;
        series.tooltip.label.padding(12, 12, 12, 12)
        series.legendSettings.labelText = this.translate.instant(intlLabel);

        // Make bullets grow on hover
        let bullet = series.bullets.push(new am4charts.CircleBullet());
        bullet.circle.strokeWidth = 2;
        bullet.circle.radius = 4;
        bullet.circle.fill = am4core.color("#fff");

        let bullethover = bullet.states.create("hover");
        bullethover.properties.scale = 1.3;
        return series;
    }

}
