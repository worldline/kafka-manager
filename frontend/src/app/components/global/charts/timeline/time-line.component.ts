import { Component, Input, Output, EventEmitter } from '@angular/core';
import { AbstractChartComponent } from '../parent/chart-abstract.component';
import { TranslateService } from '@ngx-translate/core';
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from 'ngx-toastr';

@Component({
    selector: 'app-time-line',
    templateUrl: '../parent/chart-abstract.component.html',
    styleUrls: ['../parent/chart-abstract.component.scss'],
})
export class TimeLineComponent extends AbstractChartComponent {

    @Input() displayRefreshButton: boolean = true;
    @Input() title: string;
    @Output() refresh = new EventEmitter<void>();

    constructor(protected translate: TranslateService,
                protected toastr: ToastrService,
                protected spinner: NgxSpinnerService) {
        super(translate, toastr, spinner);
    }

    createChart(am4core, am4charts, am4themes_animated, plugins) {
        // Chart code goes here
        am4core.useTheme(am4themes_animated);
        // Themes end

        // Load plugins
        const am4plugins_timeline = plugins[0];
        const am4plugins_bullets  = plugins[1];

        let chart = am4core.create(this.divId, am4plugins_timeline.SerpentineChart);
        chart.curveContainer.padding(100, 20, 50, 20);
        chart.levelCount = 3;
        chart.yAxisRadius = am4core.percent(20);
        chart.yAxisInnerRadius = am4core.percent(2);
        chart.maskBullets = false;

        chart.dateFormatter.inputDateFormat = "yyyy-MM-dd HH:mm:SS";
        chart.dateFormatter.dateFormat = "HH";
        chart.data = this.mapData(am4core, this.data);

        chart.fontSize = 10;
        chart.tooltipContainer.fontSize = 10;

        var categoryAxis = chart.yAxes.push(new am4charts.CategoryAxis());
        categoryAxis.dataFields.category = "category";
        categoryAxis.renderer.grid.template.disabled = true;
        categoryAxis.renderer.labels.template.paddingRight = 25;
        categoryAxis.renderer.minGridDistance = 10;

        var dateAxis = chart.xAxes.push(new am4charts.DateAxis());
        dateAxis.renderer.minGridDistance = 70;
        dateAxis.baseInterval = { count: 1, timeUnit: "minute" };
        dateAxis.renderer.tooltipLocation = 0;
        dateAxis.renderer.line.strokeDasharray = "1,4";
        dateAxis.renderer.line.strokeOpacity = 0.5;
        dateAxis.tooltip.background.fillOpacity = 0.2;
        dateAxis.tooltip.background.cornerRadius = 5;
        dateAxis.tooltip.label.fill = new am4core.InterfaceColorSet().getFor("alternativeBackground");
        dateAxis.tooltip.label.paddingTop = 7;
        dateAxis.endLocation = 0;
        dateAxis.startLocation = -0.5;

        let labelTemplate = dateAxis.renderer.labels.template;
        labelTemplate.verticalCenter = "middle";
        labelTemplate.fillOpacity = 0.6;
        labelTemplate.background.fill = new am4core.InterfaceColorSet().getFor("background");
        labelTemplate.background.fillOpacity = 1;
        labelTemplate.fill = new am4core.InterfaceColorSet().getFor("text");
        labelTemplate.padding(7, 7, 7, 7);

        var series = chart.series.push(new am4plugins_timeline.CurveColumnSeries());
        series.columns.template.height = am4core.percent(15);
        series.tooltip.pointerOrientation = "up";

        series.dataFields.openDateX = "start";
        series.dataFields.dateX = "end";
        series.dataFields.categoryY = "category";
        series.baseAxis = categoryAxis;
        series.columns.template.propertyFields.fill = "color"; // get color from data
        series.columns.template.propertyFields.stroke = "color";
        series.columns.template.strokeOpacity = 0;
        series.columns.template.fillOpacity = 0.6;

        var imageBullet1 = series.bullets.push(new am4plugins_bullets.PinBullet());
        imageBullet1.locationX = 1;
        imageBullet1.propertyFields.stroke = "color";
        imageBullet1.background.propertyFields.fill = "color";
        imageBullet1.image = new am4core.Image();
        imageBullet1.image.propertyFields.href = "icon";
        imageBullet1.image.scale = 0.5;
        imageBullet1.circle.radius = am4core.percent(100);
        imageBullet1.dy = -5;
        imageBullet1.tooltipText = "{text}";

        let hs = imageBullet1.states.create("hover")
        hs.properties.scale = 1.3;
        hs.properties.opacity = 1;

        /*var textBullet = series.bullets.push(new am4charts.LabelBullet());
        textBullet.label.propertyFields.text = "text";
        textBullet.disabled = true;
        textBullet.propertyFields.disabled = "textDisabled";
        textBullet.label.strokeOpacity = 0;
        textBullet.locationX = 1;
        textBullet.dy = - 100;
        textBullet.label.textAlign = "middle";*/

        chart.scrollbarX = new am4core.Scrollbar();
        chart.scrollbarX.align = "center"
        chart.scrollbarX.width = am4core.percent(75);
        chart.scrollbarX.opacity = 0.5;

        var cursor = new am4plugins_timeline.CurveCursor();
        chart.cursor = cursor;
        cursor.xAxis = dateAxis;
        cursor.yAxis = categoryAxis;
        cursor.lineY.disabled = true;
        cursor.lineX.strokeDasharray = "1,4";
        cursor.lineX.strokeOpacity = 1;

        dateAxis.renderer.tooltipLocation2 = 0;
        categoryAxis.cursorTooltipEnabled = false;

        if (this.title) {
            var label = chart.createChild(am4core.Label);
            label.text = this.title;
            label.isMeasured = false;
            label.y = am4core.percent(40);
            label.x = am4core.percent(50);
            label.horizontalCenter = "middle";
            label.fontSize = 20;
        }

        // Export menu
        this.createMenu(am4core, chart);

        return chart;
    }

    updateSeries(am4core, am4charts, chart, am4themes_animated, plugins) {
    }

    mapData(am4core, data) {
        var colorSet = new am4core.ColorSet();
        const types = [];
        return data.map(event => {
            // Manage color
            if (event.color) {
                event.color = am4core.color(event.color);
            } else if (event.type) {
                let index = types.indexOf(event.type);
                if (index < 0) {
                    index = types.length;
                    types.push(event.type);
                }
                event.color = colorSet.getIndex(index);
            } else {
                event.color = colorSet.getIndex(0);
            }

            // Manage text
            if (event.intl) {
                event.text = this.translate.instant(event.intl, event.args);
            }
            event.textDisabled = !event.text;
            return event;
        })
    }

    lazyPlugins() {
        return [
            import("@amcharts/amcharts4/plugins/timeline"),
            import("@amcharts/amcharts4/plugins/bullets")
        ];
    }

}
