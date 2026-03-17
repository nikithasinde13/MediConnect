// import { Injectable } from "@angular/core";
// import {
//   HttpInterceptor,
//   HttpRequest,
//   HttpHandler,
//   HttpEvent,
// } from "@angular/common/http";
// import { Observable } from "rxjs";
// import { AuthService } from "./auth/services/auth.service";

// @Injectable()
// export class AuthInterceptor implements HttpInterceptor {
//   constructor(private authService: AuthService) { }
//   intercept(
//     request: HttpRequest<any>,
//     next: HttpHandler
//   ): Observable<HttpEvent<any>> {
//     // Get the token from your authentication service

//     const token = this.authService.getToken();

//     if (request.url.includes("login") || request.url.includes("register")) {
//       return next.handle(request);
//     }

//     // Clone the request and add the Authorization header with the token
//     if (token) {
//       request = request.clone({
//         setHeaders: {
//           "Content-Type": "application/json; charset=utf-8",
//           Accept: "application/json",
//           Authorization: `Bearer ${token}`,
//         },
//       });
//     }

//     // Pass the modified request to the next interceptor or to the HTTP handler
//     return next.handle(request);
//   }
// }


// import { NgModule } from '@angular/core';
// import { RouterModule, Routes } from '@angular/router';

// const routes: Routes = [
//   { path: '', redirectTo: 'auth/login', pathMatch: 'full' },
//   {
//     path: 'auth',
//     loadChildren: () =>
//       import('./auth/auth.module').then(m => m.AuthModule)
//   },
//   {
//     path: 'mediconnect',
//     loadChildren: () =>
//       import('./mediconnect/mediconnect.module').then(m => m.MediconnectModule)
//   }
// ];

// @NgModule({
//   imports: [RouterModule.forRoot(routes)],
//   exports: [RouterModule]
// })
// export class AppRoutingModule {}


import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  // ✅ DEFAULT ROUTE → LOGIN
  { path: '', redirectTo: 'auth/login', pathMatch: 'full' },

  // ✅ AUTH MODULE
  {
    path: 'auth',
    loadChildren: () =>
      import('./auth/auth.module').then(m => m.AuthModule)
  },

  // ✅ MEDICONNECT MODULE
  {
    path: 'mediconnect',
    loadChildren: () =>
      import('./mediconnect/mediconnect.module').then(m => m.MediconnectModule)
  },

  // ✅ FALLBACK
  { path: '**', redirectTo: 'auth/login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
``