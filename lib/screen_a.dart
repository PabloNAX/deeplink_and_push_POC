import 'package:flutter/material.dart';

class ScreenA extends StatelessWidget {
  const ScreenA({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Container(
          width: 200,
          height: 400,
          color: Colors.amber,
        ),
      ),
    );
  }
}
