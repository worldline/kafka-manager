import { Component, Input, Output, EventEmitter, OnInit, OnChanges } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import * as uuid from 'uuid';
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from 'ngx-toastr';

@Component({
    selector: 'app-chart-abstract',
    templateUrl: './chart-abstract.component.html',
    styleUrls: ['./chart-abstract.component.scss'],
})
export abstract class AbstractChartComponent implements OnInit, OnChanges {

    @Input() data: any;
    @Input() enableLoader: boolean = true;
    divId: string = uuid.v4();
    heightStyle: string = '500px';
    spinnerId: string = 'spinner-' + uuid.v4();
    protected chart;

    constructor(protected translate: TranslateService,
                protected toastr: ToastrService,
                protected spinner: NgxSpinnerService) { }

    abstract createChart(am4core, am4charts, am4themes_animated, plugins);

    abstract updateSeries(am4core, am4charts, chart, am4themes_animated, plugins);

    abstract mapData(am4core, data);

    initChart(am4core, am4charts, am4themes_animated, plugins) {
        // Create chart
        const chart = this.createChart(am4core, am4charts, am4themes_animated, plugins);

        // Disable loading
        this.spinner.hide(this.spinnerId);

        // Store chart
        this.chart = chart;
    }

    updateChart(am4core, am4charts, am4themes_animated, plugins) {
        const chart = this.chart;

        // Update data
        chart.data = this.mapData(am4core, this.data);

        // Create series
        this.updateSeries(am4core, am4charts, chart, am4themes_animated, plugins);
        chart.validateData();

        // Disable loading
        this.spinner.hide(this.spinnerId);
    }

    createMenu(am4core, chart) {
        chart.exporting.menu = new am4core.ExportMenu();
        chart.exporting.menu.align = "left";
        chart.exporting.menu.verticalAlign = "bottom";
    }

    ngOnChanges(): void {
        if (this.chart) {
            this.lazyLoad(this.updateChart.bind(this));
        } else {
            this.lazyLoad(this.initChart.bind(this));
        }
    }

    ngOnInit(): void {
        this.lazyLoad(this.initChart.bind(this));
    }

    lazyPlugins() {
        return [];
    }

    lazyLoad(action) {
        this.spinner.show(this.spinnerId);
        if (!this.data || this.data.length < 1) {
            //return;
        }
        Promise.all([
            import("@amcharts/amcharts4/core"),
            import("@amcharts/amcharts4/charts"),
            import("@amcharts/amcharts4/themes/animated")
        ]).then(modules => {
            const plugins = this.lazyPlugins();
            return Promise.all([modules, Promise.all(plugins)]);
        }).then(data => {
            const modules = data[0];
            const am4core = modules[0];
            const am4charts = modules[1];
            const am4themes_animated = modules[2].default;
            action(am4core, am4charts, am4themes_animated, data[1]);
        }).catch((e) => {
            console.error("Error when creating chart", e);
            this.toastr.error(this.translate.instant("metrics.topic.charts.messages.error.text"), this.translate.instant("metrics.topic.charts.messages.error.title"));
        })
    }

}
