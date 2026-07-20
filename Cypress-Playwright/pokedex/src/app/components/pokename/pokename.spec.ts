import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Pokename } from './pokename';

describe('Pokename', () => {
  let component: Pokename;
  let fixture: ComponentFixture<Pokename>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Pokename]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Pokename);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
