import 'package:flutter/material.dart';

class ScreenA extends StatelessWidget {
  const ScreenA({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(
              'Screen A',
              style: Theme.of(context).textTheme.titleLarge,
            ),
            SizedBox(
              height: 24,
            ),
            Container(
              width: 200,
              height: 400,
              color: Colors.amber,
            ),
          ],
        ),
      ),
    );
  }
}
