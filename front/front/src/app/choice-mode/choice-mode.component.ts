import { Component } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale, LineController, LineElement, PointElement } from 'chart.js';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {FormsModule, ReactiveFormsModule, FormControl, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
// @ts-ignore
import * as Plotly from 'plotly.js-dist';

@Component({
  selector: 'app-choice-mode',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, CommonModule],
  templateUrl: './choice-mode.component.html',
  styleUrl: './choice-mode.component.css'
})
export class ChoiceModeComponent {
  private myChart: ChartJS | null = null;
  pointsForm!: FormGroup;
  countForm!: FormGroup;
  public ans = "";
  public data: any[] = [];
  public data2: any[] = [];
  public xValues: any[] = [];
  public yValues: any[] = [];
  public numbers: number[] = []//,4,5,6,7,8,9,10,11,12]
  public flag: boolean = false;
  constructor(private http: HttpClient) {
    ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale, LineController, LineElement, PointElement);
  }

  onSubmitCount() {
    const numberInputControl = this.countForm.get('numberInput');
    if (numberInputControl) {
      const numberInputValue = numberInputControl.value;
      for (let i = 0; i < numberInputValue; i++) {
        this.numbers.push(i);
      }
    }
    this.flag = true;
    this.pointsForm = new FormGroup({});
    this.numbers.forEach((num, index) => {
      this.pointsForm.addControl(`x${num}`, new FormControl('', Validators.required));
      this.pointsForm.addControl(`y${num}`, new FormControl('', Validators.required));
    });

  }
  onSubmit() {
    this.xValues = [];
    this.yValues = [];
    Object.keys(this.pointsForm.controls).forEach(key => {
      const control = this.pointsForm.get(key);
      if (control instanceof FormControl) {
        const keyParts = key.split('');
        if (keyParts[0] === 'x') {
          this.xValues.push(control.value);
        } else if (keyParts[0] === 'y') {
          this.yValues.push(control.value);
        }
      }
    });

   this.paint();
   const httpOptions = {
      responseType: 'text' as 'json' // This line tells Angular to expect text response
   };

   this.http.post<any>("http://localhost:8080/app-controller/points", this.pointsForm.value).subscribe(
      data => {
          this.data = data; // Присваиваем полученные данные переменной data
          this.paint();
          console.log(this.data);
      },
      error => {
        console.error('There was an error!', error);
      }
   );
  }

  ngOnInit() {
    this.countForm = new FormGroup({});
    this.countForm.addControl("numberInput", new FormControl('', [Validators.required, Validators.min(3), Validators.max(12)]));
  }
  paint() {
    const trace1 = {
        x: this.xValues,
        y: this.yValues,
        mode: 'markers',
        type: 'scatter'
      };

    const trace2 = {
      x: this.data[0],
      y: this.data[1],
      mode: 'lines',
      name: 'Lagrange',
      type: 'scatter'
    };
    const trace3 = {
      x: this.data[0],
      y: this.data[2],
      mode: 'lines',
      name: 'NewtonOfDivided',
      type: 'scatter'
    };
    const trace4 = {
      x: this.data[0],
      y: this.data[3],
      mode: 'lines',
      name: 'NewtonOfFinite',
      type: 'scatter'
    };
    let dataGraph;
    if (this.data.length > 3) {
      dataGraph = [trace1, trace2, trace3, trace4];
    } else {
      dataGraph = [trace1, trace2, trace3];
    }

    const layout = {
      title: 'Интерполяция',
      showlegend: true,
      legend: { orientation: 'h' }
    };

    Plotly.newPlot('myDiv', dataGraph, layout);
  }

}
