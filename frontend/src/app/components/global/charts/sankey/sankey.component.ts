import { Component, Input, Output, EventEmitter } from '@angular/core';
import { AbstractChartComponent } from '../parent/chart-abstract.component';
import { TranslateService } from '@ngx-translate/core';
import { NgxSpinnerService } from "ngx-spinner";
import { ToastrService } from 'ngx-toastr';

@Component({
    selector: 'app-sankey',
    templateUrl: '../parent/chart-abstract.component.html',
    styleUrls: ['../parent/chart-abstract.component.scss'],
})
export class SankeyComponent extends AbstractChartComponent {

    constructor(protected translate: TranslateService,
        protected toastr: ToastrService,
        protected spinner: NgxSpinnerService) {
        super(translate, toastr, spinner);
    }

    createChart(am4core, am4charts, am4themes_animated, plugins) {
        // Chart code goes here
        am4core.useTheme(am4themes_animated);
        // Themes end

        let chart = am4core.create(this.divId, am4charts.SankeyDiagram);
        chart.hiddenState.properties.opacity = 0;
        chart.nodes.template.nameLabel.label.width = 200;
        chart.data = this.mapData(am4core, this.data);

        let hoverState = chart.links.template.states.create("hover");
        hoverState.properties.fillOpacity = 0.6;

        chart.dataFields.fromName = "from";
        chart.dataFields.toName = "to";
        chart.dataFields.value = "value";

        // for right-most label to fit
        chart.paddingRight = 200;

        // Export menu
        this.createMenu(am4core, chart);

        return chart;
    }

    updateSeries(am4core, am4charts, chart, am4themes_animated, plugins) {
    }

    mapData(am4core, data) {
        return data;
    }

}