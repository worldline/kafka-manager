import { Component, OnInit } from '@angular/core';
import { TopicService } from '@services/topic.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Topic } from '@models/topic/topic.model';
import { Pageable } from "@models/pageable.model";

@Component({
    selector: 'app-topics',
    templateUrl: './topics.component.html'
})
export class TopicsComponent implements OnInit {

    private clusterId: string;
    topics: Topic[];
    pageable: Pageable = new Pageable();

    constructor(private route: ActivatedRoute,
                private topicService: TopicService,
                private router: Router) {
    }

    ngOnInit(): void {
        this.route.paramMap.subscribe(params => {
            if (this.route.snapshot.queryParamMap != null) {
                this.pageable.currentPage = this.route.snapshot.queryParamMap['params'].page ? this.route.snapshot.queryParamMap['params'].page : 1;
            }
            this.clusterId = params.get('clusterId');
            this.topicService.list(this.clusterId, false, this.pageable)
                .subscribe(topicPage => {
                        this.topics = topicPage.content;
                        this.pageable = Pageable.readPage(topicPage);
                    });
        });
    }

    goToCreateTopic() {
        this.router.navigateByUrl(`/clusters/${this.clusterId}/add-topic`);
    }

    pageChange(newPage: number) {
        this.pageable.currentPage = newPage;
        this.router.navigate([`/clusters/${this.clusterId}/topics`], { queryParams: { page: newPage } });
    }

}
