import 'package:flutter/material.dart';

class ScreenB extends StatelessWidget {
  const ScreenB({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(
              'Screen B',
              style: Theme.of(context).textTheme.titleLarge,
            ),
            SizedBox(
              height: 24,
            ),
            Container(
              width: 200,
              height: 400,
              color: Colors.redAccent,
            ),
          ],
        ),
      ),
    );
  }
}
