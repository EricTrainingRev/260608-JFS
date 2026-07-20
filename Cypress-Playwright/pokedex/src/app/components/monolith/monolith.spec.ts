import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Monolith } from './monolith';

describe('Monolith', () => {
  let component: Monolith;
  let fixture: ComponentFixture<Monolith>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Monolith]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Monolith);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
